package edu.cnu.swacademy.exchange.orderbook.manager;

import edu.cnu.swacademy.exchange.orderbook.OrderBook;

public interface OrderBookManager {
    OrderBook getOrderBook(int stockId);
    void clearAll();
}
