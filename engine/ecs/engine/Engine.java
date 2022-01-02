package engine.ecs.engine;

import engine.ecs.entity.EntityRegistry;

public class Engine {
    private final EntityRegistry entityRegistry;

    public Engine() {
        this.entityRegistry = new EntityRegistry();
    }

    public EntityRegistry getEntityRegistry() {
        return entityRegistry;
    }
}