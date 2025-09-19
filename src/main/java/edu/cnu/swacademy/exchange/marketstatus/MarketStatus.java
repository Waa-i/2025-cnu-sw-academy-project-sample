package edu.cnu.swacademy.exchange.marketstatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class MarketStatus {
    private int marketStatusId;
    private int stockId;
    private LocalDate tradingDate;
    private int referencePrice;
    private int upperLimitPrice;
    private int lowerLimitPrice;
    private int openingPrice;
    private int closingPrice;
    private int highestPrice;
    private int lowestPrice;
    private long tradingVolume;
    private long tradingAmount;

    public MarketStatus(int stockId, LocalDate tradingDate, int referencePrice, int upperLimitPrice, int lowerLimitPrice, int openingPrice, int closingPrice, int highestPrice, int lowestPrice, long tradingVolume, long tradingAmount) {
        this.stockId = stockId;
        this.tradingDate = tradingDate;
        this.referencePrice = referencePrice;
        this.upperLimitPrice = upperLimitPrice;
        this.lowerLimitPrice = lowerLimitPrice;
        this.openingPrice = openingPrice;
        this.closingPrice = closingPrice;
        this.highestPrice = highestPrice;
        this.lowestPrice = lowestPrice;
        this.tradingVolume = tradingVolume;
        this.tradingAmount = tradingAmount;
    }
}
