package ru.yandex.practicum.handler;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import ru.yandex.practicum.exception.SendMessageException;
import ru.yandex.practicum.exception.SerializationException;

import java.util.function.Function;

@RequiredArgsConstructor
public abstract class BaseEventHandler<T> {
    protected final KafkaProducer<String, SpecificRecordBase> kafkaProducer;

    protected void sendToKafka(T event, Function<T, SpecificRecordBase> mapper, String topic) {
        try {
            SpecificRecordBase avroEvent = mapper.apply(event);
            kafkaProducer.send(new ProducerRecord<>(topic, avroEvent),
                    (metadata, e) -> {
                        if (e != null) {
                        } else {
                        }
                    });
        } catch (SerializationException | KafkaException ex) {
            throw new SendMessageException("Ошибка при отправлении сообщения", ex);
        }
    }
}
