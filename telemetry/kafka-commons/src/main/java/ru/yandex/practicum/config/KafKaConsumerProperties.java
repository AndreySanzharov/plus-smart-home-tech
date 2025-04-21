package ru.yandex.practicum.config;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@ConfigurationProperties(prefix = "kafka.consumer.common")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class KafKaConsumerProperties {
    String bootstrapServer;
    Boolean enableAutoCommitConfig;
    String keyDeserializeClass;
    String autoOffsetReset;
    Boolean enableAutoCommit;
    String sessionTimeout;
    String heartbeatInterval;
    String maxPollInterval;
    String fetchMaxWait;
    Integer fetchMinSize;
    Integer fetchMaxBytes;
    Integer maxPartitionFetchBytes;
    Integer maxPollRecords;

    public Properties buildProperties() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        properties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, heartbeatInterval);
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollInterval);
        properties.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, fetchMaxWait);
        properties.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, fetchMinSize);
        properties.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, fetchMaxBytes);
        properties.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, maxPartitionFetchBytes);
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        return properties;
    }
}
