package engine.ecs.entity;

import engine.ecs.engine.Engine;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class EntityRegistry {
    private final Engine engine;
    private final Map<Class<?>, Entity> entities;

    public EntityRegistry(Engine engine) {
        this.engine = engine;
        this.entities = new HashMap<>();
    }

    public <E extends Entity, B extends EntityBuilder<E>> E createEntity(B builder, Consumer<B> function) {
        builder.setEngine(engine);
        function.accept(builder);
        E entity = builder.build();
        entities.put(entity.getClass(), entity);
        return entity;
    }

    public void addEntity(Entity entity) {
        entities.put(entity.getClass(), entity);
    }

    @SuppressWarnings("unchecked")
    public <E extends Entity> E getEntity(Class<E> entityClass) {
        E entity = (E) entities.get(entityClass);
        if (entity == null) {
            throw new IllegalArgumentException("Entity of class does not exist");
        }
        return entity;
    }
}
