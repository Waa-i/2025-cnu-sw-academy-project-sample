package edu.cnu.swacademy.exchange.order.dto;

import edu.cnu.swacademy.exchange.match.MatchResult;

public record CancelOrderResponse(MatchResult matchResult) {
}
