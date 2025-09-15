package edu.cnu.swacademy.exchange.engine;

import edu.cnu.swacademy.exchange.engine.adapter.MpscQueue;
import edu.cnu.swacademy.exchange.market.event.MarketEvent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MarketEventQueue {
    private final MpscQueue<MarketEvent<?>> queue;
    public void publish(MarketEvent<?> orderEvent) {
        queue.offer(orderEvent);
    }

    public MarketEvent<?> take() throws InterruptedException {
        return queue.take();
    }
}
