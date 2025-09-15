package edu.cnu.swacademy.exchange.order.listener;

import edu.cnu.swacademy.exchange.orderbook.event.ClearOrderBookEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClearOrderBookEventListener {
    private final StringRedisTemplate stringRedisTemplate;
    private static final long DEFAULT_CURSOR_COUNT = 100;
    private static final String ORDER_DETAILS_PATTERN = "order:*";
    private static final String BID_LIST_PATTERN = "*:BUY:*";
    private static final String ASK_LIST_PATTERN = "*:SELL:*";
    private static final String BID_PRICE_LIST_PATTERN = "*:BUY";
    private static final String ASK_PRICE_LIST_PATTERN = "*:SELL";

    @Async
    @EventListener(ClearOrderBookEvent.class)
    public void handleClearOrderBookEvent() {
        clearOrderDetails();
        clearBids();
        clearAsks();
        clearBidPriceList();
        clearAskPriceList();
    }
    private void clearOrderDetails() {
        clearByPattern(ORDER_DETAILS_PATTERN);
    }
    private void clearBids() {
        clearByPattern(BID_LIST_PATTERN);
    }
    private void clearAsks() {
        clearByPattern(ASK_LIST_PATTERN);
    }
    private void clearBidPriceList() {
        clearByPattern(BID_PRICE_LIST_PATTERN);
    }
    private void clearAskPriceList() {
        clearByPattern(ASK_PRICE_LIST_PATTERN);
    }
    private void clearByPattern(String pattern) {
        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(DEFAULT_CURSOR_COUNT).build();
        try (Cursor<byte[]> cursor = stringRedisTemplate.getConnectionFactory()
                .getConnection()
                .scan(options)) {
            while (cursor.hasNext()) {
                String key = new String(cursor.next());
                stringRedisTemplate.delete(key);
            }
        }
    }
}
