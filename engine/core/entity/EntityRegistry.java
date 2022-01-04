package engine.core.entity;

public class EntityRegistry {
    public <E extends Entity, B extends Entity.Builder<B, E>> B createEntity(B builder) {

    }

    public <E extends Entity> E getEntity(Class<E> entityClass) {

    }
}
