package edu.cnu.swacademy.exchange.controller;

import edu.cnu.swacademy.exchange.match.Match;
import edu.cnu.swacademy.exchange.order.dto.OrderRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/market")
public class MarketController {
    @PostMapping("/order")
    public List<Match> order(@RequestBody OrderRequest orderRequest) {

        return null;
    }
}
