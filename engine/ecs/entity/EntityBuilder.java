package engine.ecs.entity;

public abstract class EntityBuilder<E extends Entity> {
    public abstract E build();
}