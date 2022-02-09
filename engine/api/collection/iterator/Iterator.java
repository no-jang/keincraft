package engine.api.collection.iterator;

import engine.api.collection.trait.iterator.NextTrait;

public interface Iterator<T> extends java.util.Iterator<T>, NextTrait<T> {
    @Override
    default T next() {
        return NextTrait.super.next();
    }
}
