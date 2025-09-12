package edu.cnu.swacademy.exchange.orderbook.manager;

import edu.cnu.swacademy.exchange.orderbook.OrderBook;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class RedisOrderBookManager implements OrderBookManager {
    private final OrderBook orderBook;
    @Override
    public OrderBook getOrderBook(int __) {
        return orderBook;
    }
}
