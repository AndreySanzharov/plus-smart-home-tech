package ru.yandex.practicum.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.deserializer.DeserializeFactory;
import ru.yandex.practicum.deserializer.DeserializerType;

import java.util.Properties;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class KafkaConsumerFactory {
    final KafKaConsumerProperties config;
    final DeserializeFactory deserializeFactory;

    public <V extends SpecificRecordBase> KafkaConsumer<String, V> createConsumer(
            DeserializerType deserializerType,
            Class<V> valueType,
            String clientId,
            String groupId) {

        Properties properties = config.buildProperties();
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        Deserializer<V> deserializer = deserializeFactory.createDeserializer(deserializerType);
        KafkaConsumer<String, V> consumer = new KafkaConsumer<>(properties,
                new StringDeserializer(),
                deserializer);
        return consumer;
    }
}
