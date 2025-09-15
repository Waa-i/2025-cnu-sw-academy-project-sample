package edu.cnu.swacademy.exchange.market.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

@Getter
public class PriceEvent <T> extends ApplicationEvent implements ResolvableTypeProvider {
    private final T entity;

    public PriceEvent(Object source, T entity) {
        super(source);
        this.entity = entity;
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(getEntity()));
    }
}
