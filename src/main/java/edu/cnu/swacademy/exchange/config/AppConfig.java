package edu.cnu.swacademy.exchange.config;

import edu.cnu.swacademy.exchange.order.OrderEventQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration(proxyBeanMethods = true)
public class AppConfig {

    @Bean
    public OrderEventQueue orderEventQueue() {
        return new OrderEventQueue(new LinkedBlockingQueue<>());
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new ConcurrentTaskExecutor(Executors.newSingleThreadExecutor());
    }
}
