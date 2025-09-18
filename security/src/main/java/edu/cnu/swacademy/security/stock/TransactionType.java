package edu.cnu.swacademy.security.stock;

import lombok.Getter;

@Getter
public enum TransactionType {
    DIVIDEND("배당 입고"),                // 배당 주식 지급
    SELL_ORDER("매도 주문"),              // 매도 주문 → 보유 주식이 잠금(예약) 상태로 이동
    BUY_ORDER("매수 주문"),               // 매수 주문 → 주식이 잠금(예약) 상태로 이동
    TRADE_RECEIPT("구매 주문 체결)"), // 매수 체결 → 실제 주식 입고
    TRADE_DELIVERY("판매 주문 체결)"),// 매도 체결 → 실제 주식 출고
    ACCOUNT_BLOCKED("계좌 정지"),
    ACCOUNT_UNBLOCKED("계좌 정지 해제");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }
}
