package edu.cnu.swacademy.exchange.order.event;

import edu.cnu.swacademy.exchange.order.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.CompletableFuture;

@Getter
@AllArgsConstructor
public abstract class OrderEvent<T> {
    private final Order order;
    private final CompletableFuture<T> result;
}
