package edu.cnu.swacademy.exchange.orderbook.impl;

import edu.cnu.swacademy.exchange.market.event.PriceEvent;
import edu.cnu.swacademy.exchange.market.event.TotalUnitEvent;
import edu.cnu.swacademy.exchange.match.Match;
import edu.cnu.swacademy.exchange.match.MatchResult;
import edu.cnu.swacademy.exchange.order.Order;
import edu.cnu.swacademy.exchange.order.dto.PriceAddRequest;
import edu.cnu.swacademy.exchange.order.dto.PriceRemoveRequest;
import edu.cnu.swacademy.exchange.order.dto.TotalUnitAddRequest;
import edu.cnu.swacademy.exchange.order.dto.TotalUnitRemoveRequest;
import edu.cnu.swacademy.exchange.orderbook.OrderBook;
import edu.cnu.swacademy.exchange.orderbook.event.ClearOrderBookEvent;
import edu.cnu.swacademy.exchange.orderbook.exception.NoOrderException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisOrderBook implements OrderBook {

    private final StringRedisTemplate stringRedisTemplate;
    private final ApplicationEventPublisher publisher;
    @Getter
    private int lastTradePrice;

    @Override
    public List<Match> match(Order order) {
        if(order.getSide() == Order.Side.BUY) {
            return process(order, getOrder(order.getStockId(), Order.Side.SELL, order.getPrice()));
        }
        return process(order, getOrder(order.getStockId(), Order.Side.BUY, order.getPrice()));
    }

    @Override
    public Match cancel(Order order) {
        return cancelOrder(order);
    }

    @Override
    public List<Order> orders(Order.Side side, int price) {
        return List.of();
    }

    @Override
    public List<Integer> prices(int stockId, Order.Side side) {
        return getPrices(stockId, side);
    }

    @Override
    public void clear() {
        publisher.publishEvent(new ClearOrderBookEvent(this));
    }

    private Match cancelOrder(Order order) {
        if(stringRedisTemplate.hasKey(orderDetailsKey(order.getOrderId()))) {
            removeOrder(order);
            log.info("주문 취소: [주문번호:{}, 주문 수량:{}, 미체결 수량:{}]", order.getOrderId(), order.getAmount(), order.getUnfilledAmount());
            publisher.publishEvent(new TotalUnitEvent<>(this, new TotalUnitRemoveRequest(order.getStockId(), order.getSide(), order.getPrice(), order.getUnfilledAmount())));
            return new Match(order.getStockId(), MatchResult.CANCELED);
        }
        return new Match(order.getStockId(), MatchResult.UNMATCHED);
    }
    private List<Match> process(Order order, List<Order> oppositeOrders) {
        List<Match> result = new ArrayList<>();
        if(oppositeOrders.isEmpty()) {
            addOrder(order);
            result.add(new Match(order.getStockId(), MatchResult.UNMATCHED));
            log.info("미체결: [주문번호:{}, 미체결 수량:{}]", order.getOrderId(), order.getUnfilledAmount());
            return result;
        }
        while (order.getUnfilledAmount() > 0 && !oppositeOrders.isEmpty()) {
            Order oppositeOrder = oppositeOrders.getFirst();
            int filledQty = getFilledQuantity(order.getUnfilledAmount(), oppositeOrder.getUnfilledAmount());

            order.setUnfilledAmount(order.getUnfilledAmount() - filledQty);
            oppositeOrder.setUnfilledAmount(oppositeOrder.getUnfilledAmount() - filledQty);
            log.info("체결: [주문번호:{} 체결 수량:{} 미체결 수량:{}]", order.getOrderId(), filledQty, order.getUnfilledAmount());
            lastTradePrice = oppositeOrder.getPrice();
            result.add(new Match(order.getStockId(), MatchResult.MATCHED, oppositeOrder.getOrderId(), order.getOrderId()));

            if(oppositeOrder.getUnfilledAmount() == 0 && order.getUnfilledAmount() == 0) {
                removeOrder(order);
                removeOrder(oppositeOrder);
                return result;
            }
            if(oppositeOrder.getUnfilledAmount() > 0 && order.getUnfilledAmount() == 0) {
                updateUnfilledAmount(oppositeOrder.getOrderId(), oppositeOrder.getUnfilledAmount());
                return result;
            }
            if(oppositeOrder.getUnfilledAmount() == 0 && order.getUnfilledAmount() > 0) {
                oppositeOrders.removeFirst();
                removeOrder(oppositeOrder);
            }
        }
        if(order.getUnfilledAmount() > 0) {
            addOrder(order);
        }
        return result;
    }
    private List<Integer> getPrices(int stockId, Order.Side side) {
        Set<String> prices = stringRedisTemplate.opsForZSet().range(priceKey(stockId, side), 0, -1);
        if(prices == null || prices.isEmpty()) {
            throw new NoOrderException(side.name());
        }
        return prices.stream().map(Integer::parseInt).toList();
    }
    private void addOrder(Order order) {
        log.info("ADD 주문 상세: key[order:{}] -> value[{} {} {}]", order.getOrderId(), order.getAmount(), order.getUnfilledAmount(), order.getCreatedAt());
        stringRedisTemplate.opsForHash().putAll(orderDetailsKey(order.getOrderId()),
                Map.of("orderId", String.valueOf(order.getOrderId()),
                        "amount", String.valueOf(order.getAmount()),
                        "unfilledAmount", String.valueOf(order.getUnfilledAmount()),
                        "createdAt", order.getCreatedAt().toString())
        );
        log.info("ADD 주문 리스트: key[{}:{}:{}] -> value[{}]", order.getStockId(), order.getSide(), order.getPrice(), order.getOrderId());
        stringRedisTemplate.opsForList().rightPush(
                orderKey(order.getStockId(), order.getSide(), order.getPrice()),
                String.valueOf(order.getOrderId())
        );
        publisher.publishEvent(new TotalUnitEvent<>(this, new TotalUnitAddRequest(order.getOrderId(), order.getSide(), order.getPrice(), order.getUnfilledAmount())));
        publisher.publishEvent(new PriceEvent<>(this, new PriceAddRequest(order.getStockId(), order.getSide(), order.getPrice())));
    }
    private void removeOrder(Order order) {
        log.info("REMOVE 주문 상세: key[order:{}] -> value[{} {} {}]", order.getOrderId(), order.getAmount(), order.getUnfilledAmount(), order.getCreatedAt());
        stringRedisTemplate.delete(orderDetailsKey(order.getOrderId()));
        log.info("REMOVE 주문 리스트: key[{}:{}:{}] -> value[{}]", order.getStockId(), order.getSide(), order.getPrice(), order.getOrderId());
        stringRedisTemplate.opsForList().remove(orderKey(order.getStockId(), order.getSide(), order.getPrice()), 1, String.valueOf(order.getOrderId()));

        publisher.publishEvent(new PriceEvent<>(this, new PriceRemoveRequest(order.getStockId(), order.getSide(), order.getPrice())));
    }
    private List<Order> getOrder(int stockId, Order.Side side, int price) {
        List<String> orderIds = stringRedisTemplate.opsForList().range(orderKey(stockId, side, price), 0, -1);
        if(orderIds == null || orderIds.isEmpty()) {
            return Collections.emptyList();
        }
        return orderIds.stream().map(orderId -> {
            var entries = stringRedisTemplate.opsForHash().entries(orderDetailsKey(Integer.parseInt(orderId)));
            if(entries.isEmpty()) {
                return null;
            }
            return new Order(Integer.parseInt((String) entries.get("orderId")),
                    stockId, side, price,
                    Integer.parseInt((String) entries.get("amount")),
                    Integer.parseInt((String) entries.get("unfilledAmount")),
                    0,
                    LocalDateTime.parse((String) entries.get("createdAt")));
        })
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    private void updateUnfilledAmount(int orderId, int newUnfilledAmount) {
        log.info("UPDATE 주문 상세: key[order:{}] -> value[{}]", orderId, newUnfilledAmount);
        stringRedisTemplate.opsForHash().put(orderDetailsKey(orderId), "unfilledAmount", String.valueOf(newUnfilledAmount));
    }
    private String orderKey(int stockId, Order.Side side, int price) {
        return String.format("%d:%s:%d", stockId, side.name(), price);
    }
    private String orderDetailsKey(int orderId) {
        return String.format("order:%d", orderId);
    }
    private String priceKey(int stockId, Order.Side side) {
        return String.format("%d:%s", stockId, side.name());
    }
    private String totalUnitsKey(int stockId, Order.Side side) {
        return String.format("%d:%s:total-unit", stockId, side.name());
    }
}
