package edu.cnu.swacademy.exchange.orderbook;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderBookManager {
    private final Map<Integer, OrderBook> orderBookMap = new ConcurrentHashMap<>();

    public OrderBook getOrderBook(int stockId) {
        return orderBookMap.computeIfAbsent(stockId, k -> new OrderBook(stockId));
    }
}