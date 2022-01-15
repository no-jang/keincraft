package engine.collection.queue;

import engine.collection.MutableOrderedCollection;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.NoSuchElementException;

public interface MutableQueue<T> extends Queue<T>, MutableOrderedCollection<T> {
    void enqueue(T element);

    @Nullable
    T dequeueOrNull();

    default T dequeue() {
        T dequeue = dequeueOrNull();
        if (dequeue == null) {
            throw new NoSuchElementException("Can't dequeue element from queue: it is empty");
        }

        return dequeue;
    }
}
