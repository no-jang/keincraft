package engine.ecs.entity;

import engine.ecs.component.Component;
import engine.ecs.component.ComponentBuilder;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class EntityComponentBuilder<C extends Component> extends ComponentBuilder<C> {
    @Nullable
    protected EntityBuilder<?> entityBuilder;
    @Nullable
    protected Entity entity;

    protected EntityBuilder<?> getEntityBuilder() {
        if (!isInBuild || entityBuilder == null) {
            throw new IllegalStateException("EntityComponentBuilder can't use entity builder");
        }
        return entityBuilder;
    }

    @SuppressWarnings("unchecked")
    protected <B extends EntityBuilder<?>> B getEntityBuilder(Class<B> builderClass) {
        if (!entityBuilder.getClass().isAssignableFrom(builderClass)) {
            throw new IllegalArgumentException("EntityComponentBuilder is not used by EntityBuilder " + builderClass +
                    "but " + entityBuilder.getClass());
        }
        return (B) entityBuilder;
    }

    protected Entity getEntity() {
        if (!isInBuild || entity == null) {
            throw new IllegalStateException("EntityComponentBuilder can't use entity");
        }
        return entity;
    }

    @SuppressWarnings("unchecked")
    protected <E extends Entity> E getEntity(Class<E> entityClass) {
        if (!entity.getClass().isAssignableFrom(entityClass)) {
            throw new IllegalArgumentException("EntityComponentBuilder is for Entity " + entityClass +
                    "but " + entity.getClass());
        }
        return (E) entity;
    }

    void internalPreBuild(EntityBuilder<?> entityInBuild) {
        if (isInBuild) {
            throw new IllegalStateException("ComponentBuilder can't be called by EntityBuilder while in build");
        }

        isInBuild = true;
        this.entityBuilder = entityInBuild;

        preBuild();
    }

    C internalBuild() {
        if (!isInBuild) {
            throw new IllegalStateException("ComponentBuilder can't be called by EntityBuilder while not in build");
        }
        return doBuild();
    }

    void internalPostBuild() {
        if (!isInBuild) {
            throw new IllegalStateException("ComponentBuilder can't be called by EntityBuilder while not in build");
        }
        postBuild();
        isInBuild = false;
        entityBuilder = null;
    }
}
