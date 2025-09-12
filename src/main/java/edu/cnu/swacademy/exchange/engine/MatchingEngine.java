package edu.cnu.swacademy.exchange.engine;

import edu.cnu.swacademy.exchange.order.event.CancelOrderEvent;
import edu.cnu.swacademy.exchange.order.event.NewOrderEvent;
import edu.cnu.swacademy.exchange.order.event.OrderEvent;
import edu.cnu.swacademy.exchange.match.Match;
import edu.cnu.swacademy.exchange.orderbook.manager.OrderBookManager;
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
                OrderEvent<?> event = orderEventQueue.take();
                if(event instanceof NewOrderEvent orderEvent) {
                    List<Match> result = match(orderEvent);
                    orderEvent.getResult().complete(result);
                }
                else if(event instanceof CancelOrderEvent orderEvent) {
                    Match result = cancel(orderEvent);
                    orderEvent.getResult().complete(result);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    private List<Match> match(OrderEvent<?> event) {
        return orderBookManager.getOrderBook(event.getOrder().getStockId()).match(event.getOrder());
    }
    private Match cancel(OrderEvent<?> event) {
        return orderBookManager.getOrderBook(event.getOrder().getStockId()).cancel(event.getOrder());
    }
}
