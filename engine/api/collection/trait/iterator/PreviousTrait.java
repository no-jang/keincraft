package engine.api.collection.trait.iterator;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.NoSuchElementException;

public interface PreviousTrait<T> {
    @Nullable
    T previousOrNull();

    boolean hasPrevious();

    default T previous() {
        T previous = previousOrNull();
        if (previous == null) {
            throw new NoSuchElementException("Can't get previous from iterator: it's at the beginning");
        }
        return previous;
    }
}
