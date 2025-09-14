package edu.cnu.swacademy.security.order;

import edu.cnu.swacademy.security.asset.CashWallet;
import edu.cnu.swacademy.security.asset.CashWalletHistory;
import edu.cnu.swacademy.security.asset.CashWalletHistoryRepository;
import edu.cnu.swacademy.security.asset.CashWalletRepository;
import edu.cnu.swacademy.security.common.ErrorCode;
import edu.cnu.swacademy.security.common.SecurityException;
import edu.cnu.swacademy.security.market.MarketStatus;
import edu.cnu.swacademy.security.market.MarketStatusRepository;
import edu.cnu.swacademy.security.order.dto.*;
import edu.cnu.swacademy.security.stock.*;
import edu.cnu.swacademy.security.user.User;
import edu.cnu.swacademy.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;

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
                OrderSide.valueOf(orderRequest.getSide()),        // side
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

        // 5. 거래소 서버에 주문 전송 및 결과 처리 (REST template)

        RestTemplate restTemplate = new RestTemplate();
        String exchange_url = "http://localhost:8081/api/v1/market/order";
        OrderExchangeRequest body = new OrderExchangeRequest(
                order.getId(),order.getStock().getId(),order.getPrice(),order.getAmount(),order.getSide().getValue(),order.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OrderExchangeRequest> requestEntity = new HttpEntity<>(body,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                exchange_url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );


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

    /**
     * 주문 접수
     * 사용자가 특정 종목에 대해 매수/매도 주문을 접수합니다.
     *
     * @param userId 사용자 ID
     * @param request 주문 접수 요청
     * @return 주문 접수 응답
     * @throws SecurityException 주문 접수 실패 시 발생
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public OrderSubmitResponse submitOrder(int userId, OrderSubmitRequest request) throws SecurityException {
        // TODO: 주문 접수 로직을 구현하세요
        // 1. 사용자 존재 여부 확인
        // 2. 종목 존재 여부 확인
        // 3. 주문 방향 검증
        // 4. 지갑 정지 여부 검증
        // 5. 가격 틱 사이즈 검증
        // 6. 상하한가 검증
        // 7. 현금/종목 게좌 내 자산 부족 검증 및 업데이트
        // 8. 주문 생성
        // 9. Exchange 서버로 주문 전송
        // 10. Exchange 서버로부터 주문 접수 결과(match_result)에 따른 후속 처리 구현 (API 명세서 참고[link:https://hackmd.io/oauvWmzrTESwjYoSgHUegQ?both#%EC%9D%91%EB%8B%B5-%EA%B2%B0%EA%B3%BC-%EC%A4%91-%EC%A3%BC%EB%AC%B8-%EC%A0%91%EC%88%98-%EA%B2%B0%EA%B3%BCmatch_result%EC%97%90-%EB%94%B0%EB%A5%B8-%ED%9B%84%EC%86%8D-%EC%B2%98%EB%A6%AC])
        return null;
    }

    /**
     * 지갑 정지 여부 검증
     */
    private void validateWalletStatus(int userId, int stockId) throws SecurityException {
        // TODO: 지갑 정지 여부 검증 로직을 구현하세요
        // 현금 지갑 정지 여부 확인
        // 종목 지갑 정지 여부 확인
    }

    /**
     * 가격 틱 사이즈 검증
     */
    private void validateTickSize(int price) throws SecurityException {
        // TODO: 가격 틱 사이즈 검증 로직을 구현하세요
    }

    /**
     * 상하한가 검증
     */
    private void validatePriceLimits(int stockId, int price) throws SecurityException {
        // TODO: 상하한가 검증 로직을 구현하세요
        // 당일 MarketStatus 조회
        // 가격이 상한가/하한가 범위 내에 있는지 확인
    }

    /**
     * 현금/종목 게좌 내 자산 부족 검증 및 업데이트
     */
    private void validateAndUpdateWallet(int userId, int stockId, OrderSide orderSide, int price, int quantity) throws SecurityException {
        // TODO: 현금/종목 게좌 내 자산 부족 검증 및 업데이트 로직을 구현하세요
        // 매수 주문: 현금 지갑 잔액 확인 및 업데이트
        // 매도 주문: 종목 지갑 잔액 확인 및 업데이트
    }

    /**
     * 사용자의 미체결 주문 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param stockId 종목 ID (선택사항)
     * @param side 주문 방향 (선택사항)
     * @param pageable 페이징 정보
     * @return 미체결 주문 목록
     */
    public UnfilledOrdersResponse getUnfilledOrders(int userId, Integer stockId, String side, Pageable pageable) {
        // TODO: 미체결 주문 목록 조회 로직을 구현하세요
        // 조건에 따른 조회
        // DTO 변환
        return null;
    }

    /**
     * 조건에 따른 주문 조회
     *
     * @param userId 사용자 ID
     * @param stockId 종목 ID
     * @param side 주문 방향
     * @param pageable 페이징 정보
     * @return 주문 페이지
     */
    private Page<Order> getOrdersByConditions(int userId, Integer stockId, String side, Pageable pageable) {
        // TODO: 조건에 따른 주문 조회 로직을 구현하세요
        // 종목 + 방향 필터링
        // 종목 필터링만
        // 방향 필터링만
        // 필터링 없음
        return null;
    }

    /**
     * 주문 취소
     * 사용자가 이미 접수한 주문 중 미체결 수량(unfilled amount)을 모두 취소합니다.
     *
     * @param userId 사용자 ID
     * @param orderId 주문 ID
     * @return 주문 취소 응답
     * @throws SecurityException 주문 취소 실패 시 발생
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public OrderSubmitResponse cancelOrder(int userId, int orderId) throws SecurityException {
        // TODO: 주문 취소 로직을 구현하세요
        // 1. 주문 존재 여부 및 소유자 확인
        // 2. 주문 상태 확인 (취소 가능한 상태인지)
        // 3. 거래소 서버 주문 취소 API 호출
        // 4. 응답 결과 중 주문 취소 결과(Cancelled)에 따른 후속 처리
        //    - 매수 주문인 경우:
        //      - 취소된 수량 만큼 주문의 미체결 수량을 차감하고, 취소 수량을 증가
        //      - 현금지갑의 매수 주문으로 묶인 금액을 취소된 금액만큼 차감
        //      - 현금 지갑 내역 생성
        //    - 매도 주문인 경우:
        //      - 취소된 수량 만큼 주문의 미체결 수량을 차감하고, 취소 수량을 증가
        //      - 종목지갑의 매도 수량을 취소된 수량만큼 차감
        //      - 종목 지갑 내역 생성
        return null;
    }

    /**
     * Order 엔티티를 UnfilledOrderResponse로 변환
     *
     * @param order 주문 엔티티
     * @return 미체결 주문 응답 DTO
     */
    private UnfilledOrderResponse convertToUnfilledOrderResponse(Order order) {
        // TODO: Order 엔티티를 UnfilledOrderResponse로 변환하는 로직을 구현하세요
        return null;
    }
}