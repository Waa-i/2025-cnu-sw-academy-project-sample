package edu.cnu.swacademy.exchange.order;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Order {
    private int orderId;
    private int stockId;
    private Side side;
    private int price;
    private int amount;
    private int unfilledAmount;
    private int canceledAmount;
    private LocalDateTime createdAt;

    public Order(int orderId, int stockId, Side side, int price, int amount, int unfilledAmount, int canceledAmount, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.stockId = stockId;
        this.side = side;
        this.price = price;
        this.amount = amount;
        this.unfilledAmount = unfilledAmount;
        this.canceledAmount = canceledAmount;
        this.createdAt = createdAt;
    }

    public enum Side{BUY, SELL}
}
