package edu.cnu.swacademy.exchange.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {
    private int orderId;
    private int userId;
    private int stockId;
    private Side side;
    private int price;
    private int amount;
    private int unfilledAmount;
    private int canceledAmount;

    public enum Side{BUY, SELL};
}
