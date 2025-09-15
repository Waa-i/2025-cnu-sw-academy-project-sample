package edu.cnu.swacademy.exchange.market.event;

import edu.cnu.swacademy.exchange.match.Match;
import edu.cnu.swacademy.exchange.order.Order;

import java.util.concurrent.CompletableFuture;

public class CancelOrderEvent extends OrderEvent<Match> {
    public CancelOrderEvent(CompletableFuture<Match> result, Order order) {
        super(result, order);
    }
}
