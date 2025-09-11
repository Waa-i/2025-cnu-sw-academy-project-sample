package edu.cnu.swacademy.exchange.controller;

import edu.cnu.swacademy.exchange.engine.event.OrderEvent;
import edu.cnu.swacademy.exchange.match.Match;
import edu.cnu.swacademy.exchange.order.Order;
import edu.cnu.swacademy.exchange.order.OrderEventQueue;
import edu.cnu.swacademy.exchange.order.dto.OrderCancelResponse;
import edu.cnu.swacademy.exchange.order.dto.OrderRequest;
import edu.cnu.swacademy.exchange.order.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {

    private final OrderEventQueue orderEventQueue;


    @PostMapping("/order")
    public CompletableFuture<List<OrderResponse>> order(@RequestBody OrderRequest orderRequest) {
        Order order = new Order(orderRequest.orderId(),orderRequest.stockId(), orderRequest.side(), orderRequest.price(), orderRequest.amount(), orderRequest.amount(), 0, orderRequest.createdAt());
        CompletableFuture<List<Match>> result = new CompletableFuture<>();
        orderEventQueue.publish(new OrderEvent(order,result));
        return result.thenApplyAsync(this::matchToOrderResponse);
    }

    @PostMapping("/orders/{order_id}/cancel")
    public OrderCancelResponse cancel(@PathVariable int orderId) {
        return null;
    }

    private List<OrderResponse> matchToOrderResponse(List<Match> matches) {
       return matches.stream().map(match -> OrderResponse.builder()
                .matchResult(match.getMatchResult())
                .makerOrderId(match.getMakerOrderId())
                .takerOrderId(match.getTakerOrderId())
                .build()).toList();
    }
}
