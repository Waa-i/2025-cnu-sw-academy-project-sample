package edu.cnu.swacademy.exchange.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Configuration
public class AsyncConfig implements AsyncConfigurer {
    private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1;
    @Override
    public Executor getAsyncExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return ((ex, method, params) ->
            log.error("{} 메서드에서 예외 발생, 예외 메세지:{}", method.getName(), ex.getMessage())
        );
    }
}
