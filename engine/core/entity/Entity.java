package engine.core.entity;

import engine.core.Engine;
import engine.core.util.Destroyable;

import java.util.ArrayList;
import java.util.List;

public class Entity implements Destroyable {
    protected final Engine engine;
    protected final List<Entity> ascending;
    protected final List<Entity> descending;

    protected boolean isDestroyed;

    public Entity(Engine engine) {
        this.engine = engine;

        ascending = new ArrayList<>();
        descending = new ArrayList<>();
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void destroy() {
        if (isDestroyed) {
            return;
        }

        for (Entity descending : this.descending) {
            descending.destroy();
        }

        for (Entity ascending : this.ascending) {
            ascending.removeDescending(this);
        }

        destroying();

        isDestroyed = true;
    }

    protected void destroying() {

    }

    public int getMaxEntityCount() {
        return -1;
    }

    protected void withAscending(Entity ascending) {
        this.ascending.add(ascending);
    }

    protected void withDescending(Entity descending) {
        this.descending.add(descending);
    }

    protected void throwIfDestroyed() {
        if (isDestroyed) {
            throw new IllegalStateException("Object is already destroyed");
        }
    }

    public List<Entity> getAscending() {
        return ascending;
    }

    public List<Entity> getDescending() {
        return descending;
    }

    private void removeDescending(Entity descending) {
        this.descending.remove(descending);
    }

    public static abstract class Builder<E extends Entity> {
        public abstract E build();
    }
}
