package edu.cnu.swacademy.exchange.order.event;

import edu.cnu.swacademy.exchange.match.Match;
import edu.cnu.swacademy.exchange.order.Order;

import java.util.concurrent.CompletableFuture;

public class CancelOrderEvent extends OrderEvent<Match> {
    public CancelOrderEvent(Order order, CompletableFuture<Match> result) {
        super(order, result);
    }
}
