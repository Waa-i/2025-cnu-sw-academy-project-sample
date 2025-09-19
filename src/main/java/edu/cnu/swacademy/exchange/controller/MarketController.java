package edu.cnu.swacademy.exchange.controller;

import edu.cnu.swacademy.exchange.engine.MarketEventQueue;
import edu.cnu.swacademy.exchange.market.ExchangeStatus;
import edu.cnu.swacademy.exchange.market.event.CloseMarketEvent;
import edu.cnu.swacademy.exchange.market.event.OpenMarketEvent;
import edu.cnu.swacademy.exchange.match.Match;
import edu.cnu.swacademy.exchange.order.Order;
import edu.cnu.swacademy.exchange.order.dto.CancelOrderRequest;
import edu.cnu.swacademy.exchange.order.dto.CancelOrderResponse;
import edu.cnu.swacademy.exchange.order.dto.NewOrderRequest;
import edu.cnu.swacademy.exchange.order.dto.OrderResponse;
import edu.cnu.swacademy.exchange.market.event.CancelOrderEvent;
import edu.cnu.swacademy.exchange.market.event.NewOrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/api/v1/market")
@RequiredArgsConstructor
public class MarketController {

    private final MarketEventQueue marketEventQueue;


    @PostMapping("/order")
    public CompletableFuture<List<OrderResponse>> order(@RequestBody NewOrderRequest newOrderRequest) {
        Order order = Order.builder().orderId(newOrderRequest.orderId()).stockId(newOrderRequest.stockId()).side(newOrderRequest.side()).price(newOrderRequest.price()).amount(newOrderRequest.amount()).unfilledAmount(newOrderRequest.amount()).canceledAmount(0).createdAt(newOrderRequest.createdAt()).build();
        CompletableFuture<List<Match>> result = new CompletableFuture<>();
        marketEventQueue.publish(new NewOrderEvent(result, order));
        return result.thenApplyAsync(this::matchToOrderResponse);
    }

    @DeleteMapping("/cancel")
    public CompletableFuture<CancelOrderResponse> cancel(@RequestBody CancelOrderRequest cancelOrderRequest) {
        Order order = Order.builder().orderId(cancelOrderRequest.orderId()).stockId(cancelOrderRequest.stockId()).price(cancelOrderRequest.price()).side(cancelOrderRequest.side()).build();
        CompletableFuture<Match> result = new CompletableFuture<>();
        marketEventQueue.publish(new CancelOrderEvent(result, order));
        return result.thenApplyAsync(match-> new CancelOrderResponse(match.getMatchResult()));
    }

    @PostMapping("/open")
    public CompletableFuture<ExchangeStatus> open() {
        CompletableFuture<ExchangeStatus> result = new CompletableFuture<>();
        marketEventQueue.publish(new OpenMarketEvent(result));
        return result;
    }

    @PostMapping("/close")
    public CompletableFuture<ExchangeStatus> close() {
        CompletableFuture<ExchangeStatus> result = new CompletableFuture<>();
        marketEventQueue.publish(new CloseMarketEvent(result));
        return result;
    }
    private List<OrderResponse> matchToOrderResponse(List<Match> matches) {
       return matches.stream().map(match -> OrderResponse.builder()
                .matchResult(match.getMatchResult())
                .makerOrderId(match.getMakerOrderId())
                .takerOrderId(match.getTakerOrderId())
                .build()).toList();
    }
}
