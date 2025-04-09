package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "sensors")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"scenarioConditions", "scenarioActions"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Sensor {
    @Id
    @EqualsAndHashCode.Include
    String id;
    @Column(name = "hub_id", nullable = false)
    String hubId;
    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    Set<ScenarioCondition> scenarioConditions = new HashSet<>();
    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    Set<ScenarioAction> scenarioActions = new HashSet<>();
}
