package engine.core;

import engine.core.entity.EntityRegistry;

public class Engine {
    private final EntityRegistry entityRegistry;

    public Engine() {
        entityRegistry = new EntityRegistry(this);
    }

    public EntityRegistry getEntityRegistry() {
        return entityRegistry;
    }
}
