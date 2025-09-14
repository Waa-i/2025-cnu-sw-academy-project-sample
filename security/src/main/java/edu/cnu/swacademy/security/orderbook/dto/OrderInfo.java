package edu.cnu.swacademy.security.orderbook.dto;

/**
 * 오더북 내 주문 정보
 */
public record OrderInfo(
    int orderId,
    String createdAt,
    int unfilledQuantity
) {
}
