package edu.cnu.swacademy.exchange.order.dto;

import edu.cnu.swacademy.exchange.order.Order;

public record OrderRequest(int orderId, int stockId, int price, int amount, Order.Side side) {
}
