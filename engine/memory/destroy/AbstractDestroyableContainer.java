package engine.memory.destroy;

import engine.memory.container.Container;

public abstract class AbstractDestroyableContainer<T> extends AbstractDestroyable implements Container<T> {
    protected final T reference;

    public AbstractDestroyableContainer(T reference) {
        this.reference = reference;
    }

    @Override
    public T unwrap() {
        if (isDestroyed) {
            throw new IllegalStateException("Object is already destroyed");
        }

        return reference;
    }
}
