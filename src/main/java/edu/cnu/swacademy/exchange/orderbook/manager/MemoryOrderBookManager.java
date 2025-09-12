package edu.cnu.swacademy.exchange.orderbook.manager;

import edu.cnu.swacademy.exchange.orderbook.OrderBook;
import edu.cnu.swacademy.exchange.orderbook.impl.MemoryOrderBook;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//@Primary
@Component
public class MemoryOrderBookManager implements OrderBookManager {
    private final Map<Integer, OrderBook> orderBookMap = new ConcurrentHashMap<>();

    public OrderBook getOrderBook(int stockId) {
        return orderBookMap.computeIfAbsent(stockId, k -> new MemoryOrderBook());
    }
}