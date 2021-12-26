package engine.memory.reference;

import engine.memory.container.Container;
import engine.memory.owner.Owner;

public abstract class AbstractReferenceContainer<O extends Owner, T> extends AbstractReference<O> implements Container<T> {
    protected final T reference;

    public AbstractReferenceContainer(O owner, T reference) {
        super(owner);
        this.reference = reference;
    }

    @Override
    public T unwrap() {
        return reference;
    }
}
