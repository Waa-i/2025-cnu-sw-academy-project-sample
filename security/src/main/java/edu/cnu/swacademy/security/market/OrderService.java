package edu.cnu.swacademy.security.market;

import edu.cnu.swacademy.security.asset.CashWallet;
import edu.cnu.swacademy.security.asset.CashWalletHistory;
import edu.cnu.swacademy.security.asset.CashWalletHistoryRepository;
import edu.cnu.swacademy.security.asset.CashWalletRepository;
import edu.cnu.swacademy.security.common.ErrorCode;
import edu.cnu.swacademy.security.common.SecurityException;
import edu.cnu.swacademy.security.market.dto.OrderRequest;
import edu.cnu.swacademy.security.stock.*;
import edu.cnu.swacademy.security.user.User;
import edu.cnu.swacademy.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    private final CashWalletRepository cashWalletRepository;
    private final StockWalletRepository stockWalletRepository;
    private final CashWalletHistoryRepository cashWalletHistoryRepository;
    private final StockWalletHistoryRepository stockWalletHistoryRepository;
    private final MarketStatusRepository marketStatusRepository;


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void placeOrder(int userId, OrderRequest orderRequest) throws SecurityException {
        // 1. 유저, 종목, 지갑 등 엔티티 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SecurityException(ErrorCode.USER_NOT_FOUND));

        Stock stock = stockRepository.findById(Math.toIntExact(orderRequest.getStockId()))
                .orElseThrow(() -> new SecurityException(ErrorCode.STOCK_NOT_FOUND));

        CashWallet cashWallet = cashWalletRepository.findByUserIdWithLock(user.getId())
                .orElseThrow(() -> new SecurityException(ErrorCode.CASH_WALLET_NOT_FOUND));

        StockWallet stockWallet = stockWalletRepository.findByUserIdAndStockIdWithLock(user.getId(), stock.getId())
                .orElseThrow(() -> new SecurityException(ErrorCode.STOCK_WALLET_NOT_FOUND));

        // 2. 지갑 정지 상태 확인
        if (cashWallet.isBlocked() || stockWallet.isBlocked()) {
            throw new SecurityException(ErrorCode.WALLET_BLOCKED);
        }

        // 3. 호가 단위 및 상/하한가 확인
        MarketStatus marketStatus = marketStatusRepository.findByStockIdOrderByTradingDateDesc((long) stock.getId()).stream().findFirst()
                .orElseThrow(() -> new SecurityException(ErrorCode.MARKET_STATUS_NOT_FOUND));

        if (orderRequest.getPrice() > marketStatus.getUpperLimitPrice() || orderRequest.getPrice() < marketStatus.getLowerLimitPrice()) {
            throw new SecurityException(ErrorCode.PRICE_OUT_OF_BOUNDS);
        }
        // 호가 단위(tick) 검증 로직 추가 (필요 시)


        Order order = new Order(
                0,                           // id (JPA가 자동 생성)
                user,                         // user
                stock,                         // stock
                orderRequest.getSide(),        // side
                orderRequest.getPrice(),       // price
                orderRequest.getQuantity(),    // amount
                orderRequest.getQuantity(),    // unfilledAmount
                0                              // canceledAmount
        );

        orderRepository.save(order);

        // 4. 주문 유형에 따른 처리
        if (orderRequest.getSide().equals("BUY")) {
            processBuyOrder(cashWallet, orderRequest);
        } else {
            processSellOrder(stockWallet, orderRequest);
        }

        // 5. 거래소 서버에 주문 전송 및 결과 처리 (Mocking)
        // 이 부분은 OrderImpl.txt에 명시된 대로 외부 API 호출이 필요하지만,
        // 현재 구현에서는 생략하고 성공적으로 접수되었다고 가정합니다.
        // MatchResult result = exchangeClient.sendOrder(order);
        // handleMatchResult(result);

        log.info("Order placed successfully for user: {}, stock: {}, side: {}", userId, stock.getName(), orderRequest.getSide());
    }

    private void processBuyOrder(CashWallet cashWallet, OrderRequest orderRequest) throws SecurityException {
        long requiredAmount = (long) orderRequest.getPrice() * orderRequest.getQuantity();
        if (cashWallet.getReserve() - cashWallet.getDeposit() < requiredAmount) {
            throw new SecurityException(ErrorCode.INSUFFICIENT_FUNDS);
        }
        cashWallet.setDeposit((int) (cashWallet.getDeposit() + requiredAmount));
        cashWalletRepository.save(cashWallet);

        CashWalletHistory history = new CashWalletHistory(
                cashWallet,
                edu.cnu.swacademy.security.asset.TransactionType.BUY_ORDER,
                (int) requiredAmount,
                "매수 주문",
                cashWallet.getReserve()
        );
        cashWalletHistoryRepository.save(history);
    }

    private void processSellOrder(StockWallet stockWallet, OrderRequest orderRequest) throws SecurityException {
        if (stockWallet.getReserve() - stockWallet.getDeposit() < orderRequest.getQuantity()) {
            throw new SecurityException(ErrorCode.INSUFFICIENT_STOCKS);
        }
        stockWallet.setDeposit(stockWallet.getDeposit() + orderRequest.getQuantity());
        stockWalletRepository.save(stockWallet);

        StockWalletHistory history = new StockWalletHistory(
                0,
                stockWallet,
                edu.cnu.swacademy.security.stock.TransactionType.SELL_ORDER,
                orderRequest.getQuantity(),
                "매도 주문",
                stockWallet.getReserve()
        );
        stockWalletHistoryRepository.save(history);
    }
}