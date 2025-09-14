package edu.cnu.swacademy.security.orderbook;

import edu.cnu.swacademy.security.common.SecurityException;
import edu.cnu.swacademy.security.orderbook.dto.OrderBookResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 오더북 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderBookService {

  private final RedisTemplate<String, Object> redisTemplate;

  /**
   * 오더북 조회
   * 개별 종목의 오더북을 확인하여, 매수/매도 각 측면의 호가(price)와 잔량(total_quantity)을 가격별로 제공합니다.
   * 
   * @param stockId 종목 ID
   * @return 오더북 정보
   * @throws SecurityException 조회 실패 시 발생
   */
  public OrderBookResponse getOrderBook(int stockId) throws SecurityException {
    // TODO: 오더북 조회 로직을 구현하세요
    // 1. 종목 존재 여부 확인
    // 2. Redis에서 매수 호가 정보 조회
    //    - Prices: {stock_id}:{side} 키로 가격 리스트 조회
    //    - TotalUnits: {stock_id}:{side}:total-unit 키로 가격별 잔량 조회
    //    - Orders: {stock_id}:{side}:{price} 키로 주문 리스트 조회
    // 3. Redis에서 매도 호가 정보 조회
    //    - Prices: {stock_id}:{side} 키로 가격 리스트 조회
    //    - TotalUnits: {stock_id}:{side}:total-unit 키로 가격별 잔량 조회
    //    - Orders: {stock_id}:{side}:{price} 키로 주문 리스트 조회
    // 4. 데이터 정렬 및 변환
    //    - 가격별로 정렬 (매수는 내림차순, 매도는 오름차순)
    //    - 주문은 생성시점 기준 오름차순 정렬
    // 5. 응답 DTO 생성
    return null;
  }
}
