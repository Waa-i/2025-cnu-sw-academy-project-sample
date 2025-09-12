package edu.cnu.swacademy.exchange.orderbook.exception;

public class JsonSerializationException extends RuntimeException {
    public JsonSerializationException(int orderId) {
        super(String.format("order %d redis dto json serialize error", orderId));
    }
}
