package edu.cnu.swacademy.exchange.engine;

import edu.cnu.swacademy.exchange.engine.adapter.MpscQueue;
import edu.cnu.swacademy.exchange.order.event.OrderEvent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderEventQueue {
    private final MpscQueue<OrderEvent<?>> queue;
    public void publish(OrderEvent<?> orderEvent) {
        queue.offer(orderEvent);
    }

    public OrderEvent<?> take() throws InterruptedException {
        return queue.take();
    }
}
