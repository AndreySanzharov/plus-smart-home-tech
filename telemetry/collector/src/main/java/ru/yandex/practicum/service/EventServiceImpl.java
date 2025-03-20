package ru.yandex.practicum.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.KafkaException;
import ru.yandex.practicum.mapper.HubEventMapper;
import ru.yandex.practicum.mapper.SensorEventMapper;
import ru.yandex.practicum.model.hub.HubEvent;
import ru.yandex.practicum.model.sensor.SensorEvent;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "kafka.topics")
@Getter
@Setter
public class EventServiceImpl implements EventService {
    String sensorEventsTopic;
    String hubEventsTopic;
    final KafkaProducer<String, SpecificRecordBase> kafkaProducer;
    final HubEventMapper hubEventMapper;
    final SensorEventMapper sensorEventMapper;

    @Override
    public void sendSensorEvent(SensorEvent ev) {
        sendEvent(ev, sensorEventMapper::mapToAvro, sensorEventsTopic);
    }

    @Override
    public void sendHubEvent(HubEvent ev) {
        sendEvent(ev, hubEventMapper::mapToAvro, hubEventsTopic);
    }

    private <T> void sendEvent(T event, Function<T, SpecificRecordBase> mapper, String topic) {
        try {
            SpecificRecordBase avroEvent = mapper.apply(event);
            kafkaProducer.send(new ProducerRecord<>(topic, avroEvent));
        } catch (KafkaException ex) {
            throw new KafkaException("Ошибка при отправлении сообщения");
        }
    }
}
