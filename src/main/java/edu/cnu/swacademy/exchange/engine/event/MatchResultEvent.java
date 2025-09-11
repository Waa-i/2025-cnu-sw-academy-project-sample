package edu.cnu.swacademy.exchange.engine.event;

import edu.cnu.swacademy.exchange.match.Match;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MatchResultEvent extends ExchangeEvent {
    private final OrderEvent orderEvent;
    private final List<Match> result;
}
