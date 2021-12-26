package engine.memory.owner;

import engine.memory.container.Container;

public abstract class AbstractDestroyableOwnerContainer<T> extends AbstractDestroyableOwner implements Container<T> {
    protected final T reference;

    public AbstractDestroyableOwnerContainer(T reference) {
        this.reference = reference;
    }

    @Override
    public T unwrap() {
        return reference;
    }
}
