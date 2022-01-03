package engine.core.entity;

import engine.core.Engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityRegistry {
    private final Engine engine;
    private final List<Entity> entities;
    private final Map<Class<?>, Entity> builtEntities;

    public EntityRegistry(Engine engine) {
        this.engine = engine;

        entities = new ArrayList<>();
        builtEntities = new HashMap<>();
    }

    public Entity createEntity() {
        Entity entity = new Entity(engine);
        entities.add(entity);
        return entity;
    }
}
