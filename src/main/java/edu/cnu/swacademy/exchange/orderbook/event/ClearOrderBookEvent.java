package edu.cnu.swacademy.exchange.orderbook.event;

import org.springframework.context.ApplicationEvent;

public class ClearOrderBookEvent extends ApplicationEvent {
    public ClearOrderBookEvent(Object source) {
        super(source);
    }
}
