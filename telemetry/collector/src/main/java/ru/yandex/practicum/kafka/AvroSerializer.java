package ru.yandex.practicum.kafka;

import org.apache.avro.Schema;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Serializer;
import ru.yandex.practicum.exception.NullValueException;
import ru.yandex.practicum.exception.SerializationException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class AvroSerializer implements Serializer<SpecificRecordBase> {
    private final EncoderFactory encoderFactory = EncoderFactory.get();
    private final Map<Schema, DatumWriter<SpecificRecordBase>> writers = new HashMap<>();

    @Override
    public byte[] serialize(String topic, SpecificRecordBase data) {
        if (data == null) {
            throw new NullValueException("Данные для сериализации равны null");
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            BinaryEncoder encoder = encoderFactory.binaryEncoder(out, null);
            Schema schema = data.getSchema();

            DatumWriter<SpecificRecordBase> writer = writers.computeIfAbsent(
                    schema,
                    s -> {
                        return new SpecificDatumWriter<>(s);
                    });
            writer.write(data, encoder);
            encoder.flush();
            return out.toByteArray();
        } catch (IOException ex) {
            throw new SerializationException("Ошибка сериализации данных для топика " + topic, ex);
        }
    }
}
