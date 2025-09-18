package edu.cnu.swacademy.security.order.dto;

import jakarta.annotation.Nullable;

public record OrderExchangeResponse(
        String match_result,

        @Nullable
        int taker_order_id,
        @Nullable
        int maker_order_id,
        @Nullable
        String reason
) {
}
