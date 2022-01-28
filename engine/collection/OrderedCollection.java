package engine.collection;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.NoSuchElementException;

public interface OrderedCollection<T> extends Collection<T> {
    @Nullable
    T getHeadOrNull();

    default T getHead() {
        T head = getHeadOrNull();
        if (head == null) {
            throw new NoSuchElementException("Can not get head from collection: it is empty");
        }
        return head;
    }
}
