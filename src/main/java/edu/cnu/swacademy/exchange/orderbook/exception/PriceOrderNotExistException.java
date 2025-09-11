package edu.cnu.swacademy.exchange.orderbook.exception;

public class PriceOrderNotExistException extends RuntimeException {
    public PriceOrderNotExistException(String message) {
        super(message);
    }
}
