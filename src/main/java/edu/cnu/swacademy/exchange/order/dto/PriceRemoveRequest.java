package edu.cnu.swacademy.exchange.order.dto;

import edu.cnu.swacademy.exchange.order.Order;

public record PriceRemoveRequest(int stockId, Order.Side side, int price) {
}
