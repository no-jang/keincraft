package engine.memory.ownable;

import engine.memory.container.Container;
import engine.memory.owner.Owner;

public abstract class AbstractDestroyableOwnableContainer<O extends Owner, T> extends AbstractDestroyableOwnable<O> implements Container<T> {
    protected final T reference;

    public AbstractDestroyableOwnableContainer(O owner, T reference) {
        super(owner);
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
