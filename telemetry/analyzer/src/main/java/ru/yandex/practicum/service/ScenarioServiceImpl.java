package ru.yandex.practicum.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.repository.ScenarioRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class ScenarioServiceImpl implements ScenarioService {
    ScenarioRepository scenarioRepository;
    SensorService sensorService;
    ConditionService conditionService;
    ActionService actionService;

    @Override
    @Transactional
    public void addScenario(ScenarioAddedEventAvro event, String hubId) {
        Scenario scenario = scenarioRepository.findByHubIdAndName(hubId, event.getName())
                .orElseGet(() -> Scenario.builder()
                        .hubId(hubId)
                        .name(event.getName())
                        .build());
        Scenario savedScenario = scenarioRepository.save(scenario);

        processConditions(event.getConditions(), savedScenario);
        processActions(event.getActions(), savedScenario);

    }

    private void processConditions(List<ScenarioConditionAvro> conditions, Scenario scenario) {
        Set<String> sensorIds = conditions.stream()
                .map(ScenarioConditionAvro::getSensorId)
                .collect(Collectors.toSet());
        Map<String, Sensor> sensors = sensorService.findAllByIds(sensorIds);
        List<Condition> savedConditions = conditionService.saveAll(conditions);

        List<ScenarioCondition> scenarioConditions = IntStream.range(0, conditions.size())
                .mapToObj(i -> {
                    ScenarioConditionAvro avro = conditions.get(i);
                    return ScenarioCondition.builder()
                            .id(ScenarioConditionId.builder()
                                    .scenarioId(scenario.getId())
                                    .conditionId(savedConditions.get(i).getId())
                                    .sensorId(avro.getSensorId())
                                    .build())
                            .scenario(scenario)
                            .sensor(sensors.get(avro.getSensorId()))
                            .condition(savedConditions.get(i))
                            .build();
                })
                .toList();
        scenario.getScenarioConditions().addAll(scenarioConditions);
    }


    private void processActions(List<DeviceActionAvro> actions, Scenario scenario) {
        Set<String> sensorIds = actions.stream()
                .map(DeviceActionAvro::getSensorId)
                .collect(Collectors.toSet());
        Map<String, Sensor> sensors = sensorService.findAllByIds(sensorIds);
        List<Action> savedActions = actionService.saveAll(actions);

        List<ScenarioAction> scenarioActions = IntStream.range(0, actions.size())
                .mapToObj(i -> {
                    DeviceActionAvro avro = actions.get(i);
                    Action action = savedActions.get(i);
                    Sensor sensor = sensors.get(avro.getSensorId());

                    if (sensor == null) {
                        throw new IllegalArgumentException("Sensor not found: " + avro.getSensorId());
                    }

                    return ScenarioAction.builder()
                            .id(ScenarioActionId.builder()
                                    .scenarioId(scenario.getId())
                                    .sensorId(sensor.getId())
                                    .actionId(action.getId())
                                    .build())
                            .scenario(scenario)
                            .sensor(sensor)
                            .action(action)
                            .build();
                })
                .toList();

        scenario.getScenarioActions().addAll(scenarioActions);
    }

    @Override
    @Transactional
    public void removeScenario(String name, String hubId) {
        Scenario scenario = scenarioRepository.findByHubIdAndName(hubId, name)
                .orElseThrow(() -> new EntityNotFoundException("Сценарий не найден"));
        scenarioRepository.delete(scenario);
    }

    @Override
    public List<Scenario> getScenariosByHubId(String hubId) {
        List<Scenario> scenarios = scenarioRepository.findByHubId(hubId);
        scenarios.forEach(scenario -> {
            if (scenario.getScenarioConditions() != null && !scenario.getScenarioConditions().isEmpty()) {
                log.info("Условия сценария с id {}: ({}):", scenario.getId(), scenario.getScenarioConditions());
            } else {
                log.info("У сценария нет условий");
            }
        });
        scenarios.forEach(scenario -> {
            if (scenario.getScenarioConditions() != null && !scenario.getScenarioConditions().isEmpty()) {
                log.info("Действия сценария с id {}: ({}):", scenario.getId(), scenario.getScenarioActions());
            } else {
                log.info("У сценария нет действий");
            }
        });
        return scenarios;
    }
}



