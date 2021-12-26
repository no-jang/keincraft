package engine.memory.ownable;

import engine.memory.container.Container;
import engine.memory.owner.Owner;

public abstract class AbstractOwnableContainer<O extends Owner, T> extends AbstractOwnable<O> implements Container<T> {
    protected final T reference;

    public AbstractOwnableContainer(O owner, T reference) {
        super(owner);
        this.reference = reference;
    }

    @Override
    public T unwrap() {
        return reference;
    }
}
