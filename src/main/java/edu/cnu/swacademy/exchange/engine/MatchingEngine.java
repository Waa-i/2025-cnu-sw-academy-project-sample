package edu.cnu.swacademy.exchange.engine;

import edu.cnu.swacademy.exchange.engine.event.OrderEvent;
import edu.cnu.swacademy.exchange.match.Match;
import edu.cnu.swacademy.exchange.engine.event.OrderEventQueue;
import edu.cnu.swacademy.exchange.orderbook.OrderBookManager;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MatchingEngine implements Runnable {

    private final TaskExecutor taskExecutor;
    private final OrderEventQueue orderEventQueue;
    private final OrderBookManager orderBookManager;

    @PostConstruct
    public void start() {
        taskExecutor.execute(this);
    }
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                OrderEvent event = orderEventQueue.take();
                List<Match> result = process(event);
                event.getResult().complete(result);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    private List<Match> process(OrderEvent event) {
        return orderBookManager.getOrderBook(event.getOrder().getStockId()).match(event.getOrder());
    }
}
