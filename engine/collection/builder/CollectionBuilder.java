package engine.collection.builder;

import engine.collection.Collection;

public interface CollectionBuilder<T, C extends Collection<T>> {
    CollectionBuilder<T, C> add(T element);

    CollectionBuilder<T, C> remove(T element);

    CollectionBuilder<T, C> reset();

    C build();

    default CollectionBuilder<T, C> addAll(Iterable<T> elements) {
        for (T element : elements) {
            add(element);
        }
        return this;
    }

    default CollectionBuilder<T, C> removeAll(Iterable<T> elements) {
        for (T element : elements) {
            remove(element);
        }
        return this;
    }
}
