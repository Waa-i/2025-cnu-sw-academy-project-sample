package edu.cnu.swacademy.exchange.orderbook;

import edu.cnu.swacademy.exchange.match.Match;
import edu.cnu.swacademy.exchange.order.Order;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class OrderBook {
    private final int stockId;
    private final Int2ObjectMap<Queue<Order>> bids = new Int2ObjectOpenHashMap<>();
    private final Int2ObjectMap<Queue<Order>> asks = new Int2ObjectOpenHashMap<>();

    public OrderBook(int stockId) {
        this.stockId = stockId;
    }

    public List<Match> match(Order order) {
        if(order.getSide() == Order.Side.BUY) {
            return process(order, asks, bids);
        }

        return process(order, bids, asks);
    }
    private List<Match> process(Order order, Int2ObjectMap<Queue<Order>> opposite, Int2ObjectMap<Queue<Order>> same) {
        List<Match> result = new ArrayList<>();
        Queue<Order> oppositeQueue = opposite.get(order.getPrice());
        if(oppositeQueue == null || oppositeQueue.isEmpty()) {
            same.computeIfAbsent(order.getPrice(), k -> new ArrayDeque<>()).add(order);
            return result;
        }
        while (order.getUnfilledAmount() > 0 && !oppositeQueue.isEmpty()) {
            Order oppositeOrder = oppositeQueue.peek();
            int filledQty = Math.min(order.getUnfilledAmount(), oppositeOrder.getUnfilledAmount());

            order.setUnfilledAmount(order.getUnfilledAmount() - filledQty);
            oppositeOrder.setUnfilledAmount(oppositeOrder.getUnfilledAmount() - filledQty);

            result.add(new Match());

            if(oppositeOrder.getUnfilledAmount() == 0) {
                oppositeQueue.poll();
            }
        }
        if(order.getUnfilledAmount() > 0) {
            same.computeIfAbsent(order.getPrice(), k -> new ArrayDeque<>()).add(order);
        }
        return result;
    }
}
