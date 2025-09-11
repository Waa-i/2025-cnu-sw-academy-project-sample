package edu.cnu.swacademy.exchange.engine.event;

import edu.cnu.swacademy.exchange.match.Match;
import edu.cnu.swacademy.exchange.order.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Getter
@AllArgsConstructor
public class OrderEvent {
    private final Order order;
    private final CompletableFuture<List<Match>> result;
}
