package edu.cnu.swacademy.exchange.orderbook.impl;

import edu.cnu.swacademy.exchange.match.Match;
import edu.cnu.swacademy.exchange.match.MatchResult;
import edu.cnu.swacademy.exchange.order.Order;
import edu.cnu.swacademy.exchange.orderbook.OrderBook;
import edu.cnu.swacademy.exchange.orderbook.exception.PriceOrderNotExistException;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.ArrayList;
import java.util.List;

public class MemoryOrderBook implements OrderBook {

    private final Int2ObjectMap<Int2ObjectMap<Order>> bids = new Int2ObjectOpenHashMap<>();
    private final Int2ObjectMap<Int2ObjectMap<Order>> asks = new Int2ObjectOpenHashMap<>();
    private int lastTradePrice;

    @Override
    public Match cancel(Order order) {
        if(order.getSide() == Order.Side.BUY) {
            return cancel(order, bids);
        }
        return cancel(order, asks);
    }
    @Override
    public List<Match> match(Order order) {
        if(order.getSide() == Order.Side.BUY) {
            return match(order, asks, bids);
        }
        return match(order, bids, asks);
    }

    @Override
    public List<Order> orders(Order.Side side, int price) {
        if(side == Order.Side.BUY) {
            return orders(bids, price);
        }
        return orders(asks, price);
    }

    @Override
    public List<Integer> prices(int __, Order.Side side) {
        if(side == Order.Side.BUY) {
            return List.copyOf(asks.keySet());
        }
        return List.copyOf(bids.keySet());
    }

    @Override
    public void clear() {
        bids.clear();
        asks.clear();
    }

    private List<Match> match(Order order, Int2ObjectMap<Int2ObjectMap<Order>> oppositeSide, Int2ObjectMap<Int2ObjectMap<Order>> sameSide) {
        List<Match> result = new ArrayList<>();
        Int2ObjectMap<Order> oppositeSideOrderMap = oppositeSide.get(order.getPrice());
        if(oppositeSideOrderMap == null || oppositeSideOrderMap.isEmpty()) {
            sameSide.computeIfAbsent(order.getPrice(), k -> new Int2ObjectLinkedOpenHashMap<>()).put(order.getOrderId(), order);
            result.add(new Match(order.getStockId(), MatchResult.UNMATCHED));
            return result;
        }
        ObjectIterator<Order> oppositeSideOrders = oppositeSideOrderMap.values().iterator();
        while (order.getUnfilledAmount() > 0 && oppositeSideOrders.hasNext()) {
            Order oppositeSideOrder = oppositeSideOrders.next();

            int filledQty = getFilledQuantity(order.getUnfilledAmount(), oppositeSideOrder.getUnfilledAmount());
            order.setUnfilledAmount(order.getUnfilledAmount() - filledQty);
            oppositeSideOrder.setUnfilledAmount(oppositeSideOrder.getUnfilledAmount() - filledQty);
            lastTradePrice = oppositeSideOrder.getPrice();
            result.add(new Match(order.getStockId(), MatchResult.MATCHED, oppositeSideOrder.getOrderId(), order.getOrderId()));

            if(oppositeSideOrder.getUnfilledAmount() == 0) {
                oppositeSideOrders.remove();
            }
        }
        if(order.getUnfilledAmount() > 0) {
            sameSide.computeIfAbsent(order.getPrice(), k -> new Int2ObjectLinkedOpenHashMap<>()).put(order.getOrderId(), order);
        }
        return result;
    }
    private Match cancel(Order order, Int2ObjectMap<Int2ObjectMap<Order>> sameSide) {
        Int2ObjectMap<Order> sameSideOrderMap = sameSide.get(order.getPrice());
        if(sameSideOrderMap == null) {
            return new Match(order.getStockId(), MatchResult.UNMATCHED);
        }
        Order removed = sameSideOrderMap.remove(order.getOrderId());
        if(removed == null) {
            return new Match(order.getStockId(), MatchResult.UNMATCHED);
        }
        if(sameSideOrderMap.isEmpty()) {
            sameSide.remove(order.getPrice());
        }
        return new Match(order.getStockId(), MatchResult.CANCELED);
    }
    private List<Order> orders(Int2ObjectMap<Int2ObjectMap<Order>> sameSide, int price) {
        Int2ObjectMap<Order> sameSideOrderMap = sameSide.get(price);
        if(sameSideOrderMap == null) {
            throw new PriceOrderNotExistException(price);
        }
        return sameSideOrderMap.values().stream().toList();
    }
}
