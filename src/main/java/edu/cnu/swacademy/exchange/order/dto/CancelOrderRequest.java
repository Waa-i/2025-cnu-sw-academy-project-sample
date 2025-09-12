package edu.cnu.swacademy.exchange.order.dto;

public record CancelOrderRequest(int orderId, int stockId, int price) {
}
