package edu.cnu.swacademy.exchange.engine.event;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderEvents <T>  {
    private final T entity;

    public T getEntity() {
        return entity;
    }
}
