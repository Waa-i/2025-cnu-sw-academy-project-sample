package edu.cnu.swacademy.exchange.orderbook.exception;

public class PriceOrderNotExistException extends RuntimeException {
    public PriceOrderNotExistException(int price) {
        super(String.format("%d 가격에 해당하는 주문이 존재하지 않습니다.", price));
    }
}
