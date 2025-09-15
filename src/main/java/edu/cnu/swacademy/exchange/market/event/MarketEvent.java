package edu.cnu.swacademy.exchange.market.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.CompletableFuture;

@Getter
@AllArgsConstructor
public abstract class MarketEvent<T> {
    private final CompletableFuture<T> result;
}
