package edu.cnu.swacademy.security.orderbook.dto;

import java.util.List;

/**
 * 가격 레벨 정보
 */
public record PriceLevel(
    int totalQuantity,
    List<OrderInfo> orders
) {
}
