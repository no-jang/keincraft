package engine.memory.owner;

import engine.memory.container.Container;

public abstract class AbstractOwnerContainer<T> extends AbstractOwner implements Container<T> {
    protected T reference;

    public AbstractOwnerContainer(T reference) {
        super();
        this.reference = reference;
    }

    @Override
    public T unwrap() {
        return reference;
    }
}
