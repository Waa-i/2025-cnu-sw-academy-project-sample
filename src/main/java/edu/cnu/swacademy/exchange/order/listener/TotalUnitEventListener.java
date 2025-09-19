package edu.cnu.swacademy.exchange.order.listener;

import edu.cnu.swacademy.exchange.market.event.TotalUnitEvent;
import edu.cnu.swacademy.exchange.order.Order;
import edu.cnu.swacademy.exchange.order.dto.TotalUnitAddRequest;
import edu.cnu.swacademy.exchange.order.dto.TotalUnitRemoveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TotalUnitEventListener {
    private final StringRedisTemplate stringRedisTemplate;

    @Async("totalUnitEventExecutor")
    @EventListener
    public void handleTotalUnitAddEvent(TotalUnitEvent<TotalUnitAddRequest> event) {
        TotalUnitAddRequest entity = event.getEntity();
        String key = totalUnitsKey(entity.stockId(), entity.side());
        String field = String.valueOf(entity.price());
        stringRedisTemplate.opsForHash().increment(key, field, entity.unfilledAmount());
        log.info("ADD 가격별 재고 리스트: {} {}", entity.price(), entity.unfilledAmount());
    }
    @Async("totalUnitEventExecutor")
    @EventListener
    public void handleTotalUnitRemoveEvent(TotalUnitEvent<TotalUnitRemoveRequest> event) {
        TotalUnitRemoveRequest entity = event.getEntity();
        String key = totalUnitsKey(entity.stockId(), entity.side());
        String field = String.valueOf(entity.price());

        Long remaining = stringRedisTemplate.opsForHash().increment(key, field, -entity.unfilledAmount());

        if (remaining <= 0) {
            stringRedisTemplate.opsForHash().delete(key, field);
            log.info("REMOVE 가격별 재고 리스트: (남은 수량 0 {} 가격 필드 삭제)", entity.price());
        } else {
            log.info("DECREMENT 가격별 재고 리스트: {} -{} (남은 수량={})", entity.price(), entity.unfilledAmount(), remaining);
        }
    }

    private String totalUnitsKey(int stockId, Order.Side side) {
        return String.format("%d:%s:total-unit", stockId, side.name());
    }
}
