package edu.cnu.swacademy.exchange.order.dto;

import edu.cnu.swacademy.exchange.order.Order;

public record TotalUnitAddRequest(int stockId, Order.Side side, int price, int unfilledAmount) {
}
