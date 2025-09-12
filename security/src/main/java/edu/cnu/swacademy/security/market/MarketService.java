package edu.cnu.swacademy.security.market;


import edu.cnu.swacademy.security.common.BaseEntity;
import edu.cnu.swacademy.security.market.dto.MarketResponse;
import edu.cnu.swacademy.security.market.exception.MarketAlreadyOpenException;
import edu.cnu.swacademy.security.stock.Stock;
import edu.cnu.swacademy.security.stock.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


@Service
public class MarketService {

    private final StockRepository stockRepository;
    private final MatchRepository matchRepository;
    private final MarketStatusRepository marketStatusRepository;

    private final String exchangeHost = "localhost"; // 거래소 서버 호스트
    private final int exchangePort = 8081;                 // 거래소 서버 포트
    private final int timeoutMs = 2000;                  // 연결 타임아웃 2초

    // 처음 개장 시각 저장
    private final AtomicReference<OffsetDateTime> openedAt = new AtomicReference<>(null);

    public MarketService(StockRepository stockRepository,
                         MatchRepository matchRepository,
                         MarketStatusRepository marketStatusRepository) {
        this.stockRepository = stockRepository;
        this.matchRepository = matchRepository;
        this.marketStatusRepository = marketStatusRepository;
    }


    public MarketResponse openMarket() throws MarketAlreadyOpenException {
        OffsetDateTime currentOpenedAt = openedAt.get();

        if (currentOpenedAt != null) {
            // 이미 개장되어 있음
            throw new MarketAlreadyOpenException();
        }

        // TCP 연결 시도
        boolean connected = checkMarketServer();
        if (connected) {
            // 연결 성공 → 장 오픈 기록
            openedAt.compareAndSet(null, OffsetDateTime.now());
            return new MarketResponse("RUNNING", openedAt.get());
        } else {
            // 연결 실패 → 장 미개장
            return new MarketResponse("STOPPED", null);
        }
    }

    private boolean checkMarketServer() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(exchangeHost, exchangePort), timeoutMs);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean isMarketOpen() {
        return openedAt.get() != null;
    }

    public OffsetDateTime getOpenedAt() {
        return openedAt.get();
    }

    public MarketResponse closeMarket(){
        //Market Status 생성

        //거래소 서버 종료확인

        // Not Impl Yet

        return null;
    }

    @Transactional
    public List<MarketStatus> closeAllMarkets(LocalDate tradingDate) {
        if (tradingDate == null) {
            tradingDate = LocalDate.now();
        }

        List<Stock> stocks = stockRepository.findAll();
        List<MarketStatus> result = new ArrayList<>();

        for (Stock stock : stocks) {
            // BaseEntity.createdAt 기준으로 당일 Match 필터링
            LocalDate finalTradingDate = tradingDate;
            List<Match> matches = matchRepository.findByStockId((long) stock.getId()).stream()
                    .filter(m -> m.getCreatedAt().toLocalDate().equals(finalTradingDate))
                    .sorted(Comparator.comparing(BaseEntity::getCreatedAt)) // 시간 순서
                    .toList();

            int openingPrice = 0;
            int closingPrice = 0;
            int highestPrice = 0;
            int lowestPrice = 0;
            int tradingVolume = 0;
            long tradingAmount = 0;

            if (!matches.isEmpty()) {
                openingPrice = matches.get(0).getTakerOrder().getPrice();
                closingPrice = matches.get(matches.size() - 1).getTakerOrder().getPrice();
                highestPrice = matches.stream()
                        .mapToInt(m -> m.getTakerOrder().getPrice())
                        .max()
                        .orElse(openingPrice);
                lowestPrice = matches.stream()
                        .mapToInt(m -> m.getTakerOrder().getPrice())
                        .min()
                        .orElse(openingPrice);
                tradingVolume = matches.stream()
                        .mapToInt(m -> m.getTakerOrder().getAmount() - m.getTakerOrder().getUnfilledAmount())
                        .sum();
                tradingAmount = matches.stream()
                        .mapToLong(m -> (long) m.getTakerOrder().getPrice() *
                                (m.getTakerOrder().getAmount() - m.getTakerOrder().getUnfilledAmount()))
                        .sum();
            }

            int referencePrice = getPreviousClosePrice(stock, tradingDate);
            int upperLimitPrice = (int) Math.round(referencePrice * 1.3);
            int lowerLimitPrice = (int) Math.round(referencePrice * 0.7);

            MarketStatus status = new MarketStatus();
            status.setStock(stock);
            status.setTradingDate(tradingDate);
            status.setReferencePrice(referencePrice);
            status.setUpperLimitPrice(upperLimitPrice);
            status.setLowerLimitPrice(lowerLimitPrice);
            status.setOpeningPrice(openingPrice);
            status.setClosingPrice(closingPrice);
            status.setHighestPrice(highestPrice);
            status.setLowestPrice(lowestPrice);
            status.setTradingVolume(tradingVolume);
            status.setTradingAmount(tradingAmount);

            marketStatusRepository.save(status);
            result.add(status);
        }

        return result;
    }

    private int getPreviousClosePrice(Stock stock, LocalDate tradingDate) {
        return marketStatusRepository
                .findByStockIdAndTradingDate((long) stock.getId(), tradingDate.minusDays(1))
                .map(MarketStatus::getClosingPrice)
                .orElseGet(() -> {
                    // 당일 체결 데이터 중 가장 첫 거래 가격을 fallback 으로 사용
                    return matchRepository.findFirstByStockIdAndCreatedAtBetweenOrderByCreatedAtAsc(
                                    stock.getId(),
                                    tradingDate.atStartOfDay(),
                                    tradingDate.plusDays(1).atStartOfDay()
                            ).map(m -> m.getTakerOrder().getPrice())
                            .orElse(0); // 체결도 없으면 0
                });
    }
}
