package edu.cnu.swacademy.exchange.order.dto;

import edu.cnu.swacademy.exchange.match.MatchResult;
import lombok.Builder;

@Builder
public record OrderResponse(MatchResult matchResult, int makerOrderId, int takerOrderId) {
}
