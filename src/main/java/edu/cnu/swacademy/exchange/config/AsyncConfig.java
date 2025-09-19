package edu.cnu.swacademy.exchange.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Configuration
public class AsyncConfig implements AsyncConfigurer {
    @Bean(name = "priceEventExecutor")
    public Executor priceEventExecutor() {
        return Executors.newSingleThreadExecutor();
    }
    @Bean(name = "totalUnitEventExecutor")
    public Executor totalUnitEventExecutor() {
        return Executors.newSingleThreadExecutor();
    }
    @Bean(name = "ioExecutor")
    public Executor ioExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return ((ex, method, params) ->
            log.error("{} 메서드에서 예외 발생, 예외 메세지:{}", method.getName(), ex.getMessage())
        );
    }
}
