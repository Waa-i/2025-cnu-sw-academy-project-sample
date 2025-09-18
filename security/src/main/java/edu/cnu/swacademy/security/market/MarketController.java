package edu.cnu.swacademy.security.market;

import edu.cnu.swacademy.security.market.dto.MarketResponse;
import edu.cnu.swacademy.security.market.exception.MarketAlreadyOpenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/market")
@RestController
public class MarketController {

    private final MarketService marketService;

    // 장 시작
    @PostMapping("/open")
    public ResponseEntity<MarketResponse> openMarket() throws SecurityException, MarketAlreadyOpenException {
        MarketResponse response = marketService.openMarket();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/close")
    public ResponseEntity<MarketResponse> closeMarket(){
        MarketResponse response = marketService.closeMarket();
        return ResponseEntity.ok(response);
    }

    
}
