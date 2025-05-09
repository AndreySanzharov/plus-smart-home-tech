package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.exception.NotMutchException;

@Entity
@Table(name = "scenario_conditions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScenarioCondition {
    @EmbeddedId
    @EqualsAndHashCode.Include
    ScenarioConditionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("scenarioId")
    @JoinColumn(name = "scenario_id", nullable = false)
    @ToString.Exclude
    Scenario scenario;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("sensorId")
    @JoinColumn(name = "sensor_id", nullable = false)
    @ToString.Exclude
    Sensor sensor;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("conditionId")
    @JoinColumn(name = "condition_id", nullable = false)
    @ToString.Exclude
    Condition condition;

    @PrePersist
    @PreUpdate
    private void validateHubId() {
        if (!scenario.getHubId().equals(sensor.getHubId())) {
            throw new NotMutchException(String.format("id хабов не совпадают для сценария {} и для сенсора {}",
                    scenario.getId(), sensor.getId()));
        }
    }
}
