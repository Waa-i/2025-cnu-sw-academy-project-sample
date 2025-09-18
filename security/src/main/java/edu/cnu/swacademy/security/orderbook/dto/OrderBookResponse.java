package edu.cnu.swacademy.security.orderbook.dto;

import java.util.Map;

/**
 * 오더북 조회 응답
 */
public record OrderBookResponse(
    Map<String, PriceLevel> buy,
    Map<String, PriceLevel> sell
) {
}
