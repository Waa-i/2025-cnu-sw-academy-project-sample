package edu.cnu.swacademy.exchange.order.listener;

import edu.cnu.swacademy.exchange.order.Order;
import edu.cnu.swacademy.exchange.order.dto.PriceAddRequest;
import edu.cnu.swacademy.exchange.order.dto.PriceRemoveRequest;
import edu.cnu.swacademy.exchange.market.event.PriceEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PriceEventListener {
    private final StringRedisTemplate stringRedisTemplate;

    @Async
    @EventListener
    public void handlePriceAddEvent(PriceEvent<PriceAddRequest> event) {
        PriceAddRequest entity = event.getEntity();
        String key = priceKey(entity.stockId(), entity.side());
        String price = String.valueOf(entity.price());
        List<String> prices = stringRedisTemplate.opsForList().range(key, 0, -1);
        if(prices == null || !prices.contains(price)) {
            log.info("ADD 종목번호:{} {} 호가 가격 정보:{}", entity.stockId(), entity.side(), price);
            stringRedisTemplate.opsForList().rightPush(key, price);
        }
    }

    @Async
    @EventListener
    public void handlePriceRemoveEvent(PriceEvent<PriceRemoveRequest> event) {
        PriceRemoveRequest entity = event.getEntity();
        String orderKey = orderKey(entity.stockId(), entity.side(), entity.price());
        Long size = stringRedisTemplate.opsForList().size(orderKey);
        if(size == null || size == 0) {
            log.info("REMOVE 종목번호:{}의 {} 호가 가격 정보:{}", entity.stockId(), entity.side(), entity.price());
            stringRedisTemplate.opsForList().remove(priceKey(event.getEntity().stockId(), event.getEntity().side()), 1, String.valueOf(event.getEntity().price()));
        }
    }
    private String priceKey(int stockId, Order.Side side) {
        return String.format("%s:%s", stockId, side);
    }
    private String orderKey(int stockId, Order.Side side, int price) {
        return String.format("%d:%s:%d", stockId, side.name(), price);
    }
}
