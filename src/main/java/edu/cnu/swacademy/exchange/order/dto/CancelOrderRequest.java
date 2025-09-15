package edu.cnu.swacademy.exchange.order.dto;

import edu.cnu.swacademy.exchange.order.Order;

public record CancelOrderRequest(int orderId, int stockId, int price, Order.Side side) {
}
