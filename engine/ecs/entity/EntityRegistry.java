package engine.ecs.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class EntityRegistry {
    private final Map<Class<?>, Entity> entities;

    public EntityRegistry() {
        this.entities = new HashMap<>();
    }

    public <E extends Entity, B extends EntityBuilder<E>> E createEntity(B builder, Consumer<B> function) {
        function.accept(builder);
        E entity = builder.build();
        entities.put(entity.getClass(), entity);
        return entity;
    }

    @SuppressWarnings("unchecked")
    public <E extends Entity> E getEntity(Class<E> entityClass) {
        return (E) entities.get(entityClass);
    }
}
