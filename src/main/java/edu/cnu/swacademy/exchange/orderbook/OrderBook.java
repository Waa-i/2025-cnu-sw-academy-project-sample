package edu.cnu.swacademy.exchange.orderbook;

import edu.cnu.swacademy.exchange.match.Match;
import edu.cnu.swacademy.exchange.order.Order;

import java.util.List;

public interface OrderBook {
    List<Match> match(Order order);
    List<Order> orders(Order.Side side, int price);
    List<Integer> prices(Order.Side side);
    Match cancel(Order order);
    default int getFilledQuantity(int unfilledAmount, int oppositeUnfilledAmount) {
        return Math.min(unfilledAmount, oppositeUnfilledAmount);
    }
}
