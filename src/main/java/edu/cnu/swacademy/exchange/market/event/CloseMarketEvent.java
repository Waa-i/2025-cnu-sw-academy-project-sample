package edu.cnu.swacademy.exchange.market.event;

import edu.cnu.swacademy.exchange.market.ExchangeStatus;

import java.util.concurrent.CompletableFuture;

public class CloseMarketEvent extends MarketEvent<ExchangeStatus> {
    public CloseMarketEvent(CompletableFuture<ExchangeStatus> result) {
        super(result);
    }
}
