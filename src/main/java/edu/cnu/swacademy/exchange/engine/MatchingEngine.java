package edu.cnu.swacademy.exchange.engine;

import edu.cnu.swacademy.exchange.engine.event.MatchResultEvent;
import edu.cnu.swacademy.exchange.engine.event.OrderEvent;
import edu.cnu.swacademy.exchange.match.Match;
import edu.cnu.swacademy.exchange.orderbook.OrderBook;
import edu.cnu.swacademy.exchange.orderbook.OrderBookManager;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class MatchingEngine implements Runnable {
    private final BlockingQueue<OrderEvent> orderQueue = new LinkedBlockingQueue<>();
    private final TaskExecutor taskExecutor = new ConcurrentTaskExecutor(Executors.newSingleThreadExecutor());
    private final OrderBookManager orderBookManager;
    private final ApplicationEventPublisher publisher;
    @Autowired
    public MatchingEngine(OrderBookManager orderBookManager, ApplicationEventPublisher publisher) {
        this.orderBookManager = orderBookManager;
        this.publisher = publisher;
    }

    @PostConstruct
    public void start() {
        taskExecutor.execute(this);
    }
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                OrderEvent event = orderQueue.take();
                List<Match> result = process(event);
                publisher.publishEvent(new MatchResultEvent(event, result));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    private List<Match> process(OrderEvent event) {
        return orderBookManager.getOrderBook(event.getOrder().getStockId()).match(event.getOrder());
    }
}
