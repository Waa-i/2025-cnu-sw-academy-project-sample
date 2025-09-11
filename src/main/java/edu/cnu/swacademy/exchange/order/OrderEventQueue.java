package edu.cnu.swacademy.exchange.order;

import edu.cnu.swacademy.exchange.engine.event.OrderEvent;

import java.util.concurrent.BlockingQueue;

public class OrderEventQueue {
    private final BlockingQueue<OrderEvent> queue;

    public OrderEventQueue(BlockingQueue<OrderEvent> queue) {
        this.queue = queue;
    }

    public void publish(OrderEvent orderEvent) {
        queue.offer(orderEvent);
    }

    public OrderEvent take() throws InterruptedException {
        return queue.take();
    }
}
