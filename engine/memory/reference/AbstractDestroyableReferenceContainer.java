package engine.memory.reference;

import engine.memory.container.Container;
import engine.memory.owner.Owner;

public abstract class AbstractDestroyableReferenceContainer<O extends Owner, T> extends AbstractDestroyableReference<O> implements Container<T> {
    protected final T reference;

    public AbstractDestroyableReferenceContainer(O owner, T reference) {
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
