package edu.cnu.swacademy.security.orderbook;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.cnu.swacademy.security.common.SecurityException;
import edu.cnu.swacademy.security.orderbook.dto.OrderBookResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 오더북 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/orderbook")
@RequiredArgsConstructor
public class OrderBookController {

  private final OrderBookService orderBookService;

  /**
   * 오더북 조회
   * 개별 종목의 오더북을 확인하여, 매수/매도 각 측면의 호가(price)와 잔량(total_quantity)을 가격별로 제공합니다.
   * 
   * @param stockId 종목 ID
   * @return 오더북 정보
   * @throws SecurityException 조회 실패 시 발생
   */
  @GetMapping("/{stock_id}")
  public OrderBookResponse getOrderBook(@PathVariable("stock_id") int stockId) throws SecurityException {
    return orderBookService.getOrderBook(stockId);
  }
}
