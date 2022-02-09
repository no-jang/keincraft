package engine.api.collection.trait.iterator;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.NoSuchElementException;

public interface NextTrait<T> {
    @Nullable
    T nextOrNull();

    boolean hasNext();

    default T next() {
        T next = nextOrNull();
        if (next == null) {
            throw new NoSuchElementException("Can't get next from iterator: it's at the end");
        }
        return next;
    }
}
