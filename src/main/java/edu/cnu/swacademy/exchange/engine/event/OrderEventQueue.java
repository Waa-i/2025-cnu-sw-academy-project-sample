package edu.cnu.swacademy.exchange.engine.event;

import edu.cnu.swacademy.exchange.engine.event.adapter.MpscQueue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderEventQueue {
    private final MpscQueue<OrderEvent> queue;
    public void publish(OrderEvent orderEvent) {
        queue.offer(orderEvent);
    }

    public OrderEvent take() throws InterruptedException {
        return queue.take();
    }
}
