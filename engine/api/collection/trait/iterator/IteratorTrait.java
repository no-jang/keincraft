package engine.api.collection.trait.iterator;

import engine.api.collection.iterator.Iterator;

public interface IteratorTrait<T> extends Iterable<T> {
    Iterator<T> iterator();
}
