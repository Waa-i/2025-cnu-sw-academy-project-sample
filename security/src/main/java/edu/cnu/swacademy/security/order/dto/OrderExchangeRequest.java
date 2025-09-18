package edu.cnu.swacademy.security.order.dto;

public record OrderExchangeRequest(
        int orderId,
        int stockId,
        int price,
        int amount,
        String side,
        String createAt) {

}
