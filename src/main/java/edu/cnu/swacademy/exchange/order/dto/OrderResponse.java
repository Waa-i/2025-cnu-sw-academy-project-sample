package edu.cnu.swacademy.exchange.order.dto;

import edu.cnu.swacademy.exchange.match.Match;
import lombok.Builder;

@Builder
public record OrderResponse(Match.MatchResult matchResult, int makerOrderId, int takerOrderId) {
}
