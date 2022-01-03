package engine.core;

import engine.core.entity.EntityRegistry;
import engine.core.event.EventRegistry;
import engine.core.system.SystemRegistry;

public class Engine {
    private final EntityRegistry entityRegistry;
    private final SystemRegistry systemRegistry;
    private final EventRegistry eventRegistry;

    public Engine() {
        entityRegistry = new EntityRegistry(this);
        systemRegistry = new SystemRegistry(this);
        eventRegistry = new EventRegistry(this);
    }

    public EntityRegistry getEntityRegistry() {
        return entityRegistry;
    }

    public SystemRegistry getSystemRegistry() {
        return systemRegistry;
    }

    public EventRegistry getEventRegistry() {
        return eventRegistry;
    }
}
