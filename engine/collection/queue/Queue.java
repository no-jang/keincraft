package engine.collection.queue;

import engine.collection.OrderedCollection;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.NoSuchElementException;

public interface Queue<T> extends OrderedCollection<T> {
    @Nullable
    T peekOrNull();

    default T peek() {
        T peek = peekOrNull();
        if (peek == null) {
            throw new NoSuchElementException("Can't peek element from queue: it is empty");
        }
        return peek;
    }
}
