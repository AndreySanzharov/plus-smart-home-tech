package ru.yandex.practicum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AggregatorExecutorConfig {
    @Bean(name = "eventToSnapshotExecutor")
    public ExecutorService snapshotExecutor() {
        return Executors.newSingleThreadExecutor(r -> {
                    return new Thread(r, "event-to-snapshot-worker");
                }
        );
    }
}
