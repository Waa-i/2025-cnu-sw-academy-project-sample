package edu.cnu.swacademy.exchange.order.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OrderRedisDto(int orderId, int amount, int unfilledAmount, LocalDateTime createdAt) {
}
