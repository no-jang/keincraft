package engine.common.collection;

import engine.api.collection.Collection;

public abstract class AbstractCollection<T> implements Collection<T> {
    protected int size;

    public AbstractCollection() {
        this.size = 0;
    }

    @Override
    public int size() {
        return size;
    }
}
