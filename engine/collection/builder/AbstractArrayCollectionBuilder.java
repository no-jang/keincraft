package engine.collection.builder;

import engine.collection.Collection;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractArrayCollectionBuilder<T, C extends Collection<T>> implements CollectionBuilder<T, C> {
    protected static final int INITIAL_CAPACITY = 10;

    // TODO Replace with own list
    protected List<T> elements;

    public AbstractArrayCollectionBuilder() {
        this(INITIAL_CAPACITY);
    }

    public AbstractArrayCollectionBuilder(int initialCapacity) {
        elements = new ArrayList<>();
    }

    @Override
    public CollectionBuilder<T, C> add(T element) {
        elements.add(element);
        return this;
    }

    @Override
    public CollectionBuilder<T, C> remove(T element) {
        elements.remove(element);
        return this;
    }

    @Override
    public CollectionBuilder<T, C> reset() {
        elements = new ArrayList<>();
        return this;
    }
}
