package edu.cnu.swacademy.exchange.order.dto;

import edu.cnu.swacademy.exchange.order.Order;

import java.time.LocalDateTime;

public record NewOrderRequest(int orderId, int stockId, int price, int amount, Order.Side side, LocalDateTime createdAt) {
}
