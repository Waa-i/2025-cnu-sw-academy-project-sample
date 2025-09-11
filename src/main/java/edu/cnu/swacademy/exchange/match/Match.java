package edu.cnu.swacademy.exchange.match;

import lombok.Getter;

@Getter
public class Match {
    private int matchId;
    private int stockId;
    private MatchResult matchResult;
    private int makerOrderId;
    private int takerOrderId;

    public Match(int stockId, MatchResult matchResult) {
        this.stockId = stockId;
        this.matchResult = matchResult;
    }

    public Match(int stockId, MatchResult matchResult, int makerOrderId, int takerOrderId) {
        this.stockId = stockId;
        this.matchResult = matchResult;
        this.makerOrderId = makerOrderId;
        this.takerOrderId = takerOrderId;
    }

}
