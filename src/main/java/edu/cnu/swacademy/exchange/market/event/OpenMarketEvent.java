package edu.cnu.swacademy.exchange.market.event;

import edu.cnu.swacademy.exchange.market.ExchangeStatus;

import java.util.concurrent.CompletableFuture;

public class OpenMarketEvent extends MarketEvent<ExchangeStatus> {
    public OpenMarketEvent(CompletableFuture<ExchangeStatus> result) {
        super(result);
    }
}
