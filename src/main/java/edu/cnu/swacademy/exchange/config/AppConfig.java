package edu.cnu.swacademy.exchange.config;

import edu.cnu.swacademy.exchange.engine.event.OrderEvent;
import edu.cnu.swacademy.exchange.engine.event.OrderEventQueue;
import edu.cnu.swacademy.exchange.engine.event.adapter.MpscQueue;
import org.jctools.queues.MessagePassingQueue;
import org.jctools.queues.MpscLinkedQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration(proxyBeanMethods = true)
public class AppConfig {
    @Bean
    public MpscQueue<OrderEvent> orderEventMpscQueue() {
        return new MpscQueue<>(new MpscLinkedQueue<>());
    }

    @Bean
    public OrderEventQueue orderEventQueue() {
        return new OrderEventQueue(orderEventMpscQueue());
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new ConcurrentTaskExecutor(Executors.newSingleThreadExecutor());
    }


}
