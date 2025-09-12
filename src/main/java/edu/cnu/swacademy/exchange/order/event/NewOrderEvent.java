package edu.cnu.swacademy.exchange.order.event;

import edu.cnu.swacademy.exchange.match.Match;
import edu.cnu.swacademy.exchange.order.Order;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NewOrderEvent extends OrderEvent<List<Match>> {
    public NewOrderEvent(Order order, CompletableFuture<List<Match>> result) {
        super(order, result);
    }
}
