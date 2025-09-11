package edu.cnu.swacademy.exchange.engine.event;

import edu.cnu.swacademy.exchange.order.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderEvent extends ExchangeEvent {
    private final Order order;
}
