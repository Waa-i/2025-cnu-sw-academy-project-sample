package edu.cnu.swacademy.exchange.config;

import edu.cnu.swacademy.exchange.engine.MarketEventQueue;
import edu.cnu.swacademy.exchange.engine.adapter.MpscQueue;
import edu.cnu.swacademy.exchange.market.event.MarketEvent;
import org.jctools.queues.MpscLinkedQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import java.util.concurrent.Executors;

@Configuration(proxyBeanMethods = true)
public class AppConfig {
    @Bean
    public MpscQueue<MarketEvent<?>> marketEventMpscQueue() {
        return new MpscQueue<>(new MpscLinkedQueue<>());
    }

    @Bean
    public MarketEventQueue marketEventQueue() {
        return new MarketEventQueue(marketEventMpscQueue());
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new ConcurrentTaskExecutor(Executors.newSingleThreadExecutor());
    }

}
