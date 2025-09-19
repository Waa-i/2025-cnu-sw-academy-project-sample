package edu.cnu.swacademy.exchange.order.listener;

import edu.cnu.swacademy.exchange.market.event.PriceEvent;
import edu.cnu.swacademy.exchange.order.Order;
import edu.cnu.swacademy.exchange.order.dto.PriceAddRequest;
import edu.cnu.swacademy.exchange.order.dto.PriceRemoveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PriceEventListener {
    private final StringRedisTemplate stringRedisTemplate;

    @Async("priceEventExecutor")
    @EventListener
    public void handlePriceAddEvent(PriceEvent<PriceAddRequest> event) {
        PriceAddRequest entity = event.getEntity();
        String key = priceKey(entity.stockId(), entity.side());
        String price = String.valueOf(entity.price());
        stringRedisTemplate.opsForZSet().add(key, price, entity.price());
        log.info("ADD 종목번호:{} {} 호가 가격 정보:{}", entity.stockId(), entity.side(), price);
    }

    @Async("priceEventExecutor")
    @EventListener
    public void handlePriceRemoveEvent(PriceEvent<PriceRemoveRequest> event) {
        PriceRemoveRequest entity = event.getEntity();
        String key = priceKey(entity.stockId(), entity.side());
        String price = String.valueOf(entity.price());
        stringRedisTemplate.opsForZSet().remove(key, price);
        log.info("REMOVE 종목번호:{}의 {} 호가 가격 정보:{}", entity.stockId(), entity.side(), price);
    }
    private String priceKey(int stockId, Order.Side side) {
        return String.format("%s:%s", stockId, side);
    }
}
