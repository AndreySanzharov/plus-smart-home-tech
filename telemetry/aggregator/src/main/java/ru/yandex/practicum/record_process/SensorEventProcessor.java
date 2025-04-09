package ru.yandex.practicum.record_process;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Optional;

@Component
@RequiredArgsConstructor

public class SensorEventProcessor implements RecordProcessor<SensorEventAvro, SensorsSnapshotAvro> {
    private final SensorSnapshotUpdater updater;

    @Override
    public Optional<SensorsSnapshotAvro> process(SensorEventAvro record) {
        return updater.updateState(record);
    }
}

