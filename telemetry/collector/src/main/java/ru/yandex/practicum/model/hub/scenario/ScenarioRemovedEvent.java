package ru.yandex.practicum.model.hub.scenario;

import ru.yandex.practicum.enums.HubEventType;
import ru.yandex.practicum.model.hub.HubEvent;

public class ScenarioRemovedEvent extends HubEvent {

    @Override
    public HubEventType getType() {
        String name;
        return HubEventType.SCENARIO_REMOVED;
    }
}
