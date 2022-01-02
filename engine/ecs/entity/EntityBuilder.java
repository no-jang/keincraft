package engine.ecs.entity;

import engine.ecs.component.Component;
import engine.ecs.engine.Engine;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class EntityBuilder<E extends Entity> {
    protected boolean isInBuild = false;
    protected List<EntityComponentBuilder<?>> components;

    @Nullable
    private Engine engine;

    public EntityBuilder() {
        this(null);
    }

    public EntityBuilder(@Nullable Engine engine) {
        this.engine = engine;
        this.components = new ArrayList<>();
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

        for (EntityComponentBuilder<?> builder : this.components) {
            builder.se
            builder.internalPreBuild(this);
        }

        E built = doBuild();

        List<Component> components = new ArrayList<>(this.components.size());
        for (EntityComponentBuilder<?> builder : this.components) {
            components.add(builder.internalBuild(built));
        }

        built.addComponents(components);

        postBuild();

        for (EntityComponentBuilder<?> builder : this.components) {
            builder.internalPostBuild();
        }

        isInBuild = false;

        return built;
    }

    protected void postBuild() {

    }

    protected abstract E doBuild();

    protected Engine getEngine() {
        if (engine == null) {
            throw new IllegalStateException("Can't use engine when it is not available");
        }
        return engine;
    }

    void setEngine(Engine engine) {
        this.engine = engine;
    }
}