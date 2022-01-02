package engine.ecs.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class EntityBuilder<E extends Entity> {
    protected boolean isInBuild = false;

    protected List<EntityComponentBuilder<?>> components;

    public EntityBuilder() {
        components = new ArrayList<>();
    }

    public <B extends EntityComponentBuilder<?>> void component(B builder, Consumer<B> function) {
        function.accept(builder);
        components.add(builder);
    }

    protected void preBuild() {

    }

    public E build() {
        if (isInBuild) {
            throw new IllegalArgumentException("EntityBuilder can't be called for new build while in build");
        }
        isInBuild = true;
        preBuild();

        for (EntityComponentBuilder<?> builder : components) {
            builder.internalPreBuild(this);
        }

        E built = doBuild();

        for (EntityComponentBuilder<?> builder : components) {
            builder.internalBuild();
        }

        postBuild();

        for (EntityComponentBuilder<?> builder : components) {
            builder.internalPostBuild();
        }

        isInBuild = false;

        return built;
    }

    protected void postBuild() {

    }

    protected abstract E doBuild();
}