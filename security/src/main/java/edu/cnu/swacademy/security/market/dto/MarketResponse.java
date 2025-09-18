package edu.cnu.swacademy.security.market.dto;

import java.time.OffsetDateTime;

public record MarketResponse(
        String engineStatus,       // RUNNING or STOPPED
        OffsetDateTime openedAt    // ISO8601 포맷 자동 변환
) { }
