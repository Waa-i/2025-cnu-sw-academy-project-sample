package edu.cnu.swacademy.exchange.market.event;

import edu.cnu.swacademy.exchange.order.Order;
import lombok.Getter;

import java.util.concurrent.CompletableFuture;

@Getter
public abstract class OrderEvent<T> extends MarketEvent<T> {
    private final Order order;

    public OrderEvent(CompletableFuture<T> result, Order order) {
        super(result);
        this.order = order;
    }
}
