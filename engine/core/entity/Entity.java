package engine.core.entity;

import engine.core.Engine;
import engine.core.util.Destroyable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Entity implements Destroyable {
    protected final Engine engine;

    public Entity(Engine engine) {
        this.engine = engine;
    }

    public void destroy() {

    }

    protected void throwIfDestroyed() {

    }

    void withAscending(List<Entity> ascending) {

    }

    void withDescending(List<Entity> descending) {

    }

    public static abstract class Builder<SELF extends Builder<SELF, E>, E extends Entity> {
        protected final Engine engine;

        public Builder(Engine engine) {
            this.engine = engine;
        }

        public <CE extends Entity, CB extends Builder<CB, CE>> SELF with(CB builder) {

        }

        public <CE extends Entity, CB extends Builder<CB, CE>> SELF with(CB builder, Consumer<CB> apply) {
            apply.accept(builder);
        }

        public <CE extends Entity> SELF with(CE entity) {

        }

        public E make() {

        }

        public int getMaxEntityCount() {
            return -1;
        }

        protected <CE extends Entity> Supplier<CE> getEntity(Class<CE> entityClass) {

        }

        protected void withAscending(Entity ascending) {

        }

        protected void withDescending(Entity descending) {

        }

        protected void preBuild() {

        }

        protected abstract E build();

        protected void postBuild() {

        }
    }
}
