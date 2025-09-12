package edu.cnu.swacademy.exchange.orderbook.exception;

public class NoOrderException extends RuntimeException {
    public NoOrderException(String side) {
        super(String.format("%s is empty", side));
    }
}
