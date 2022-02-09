package engine.api.collection.trait.order;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.NoSuchElementException;

public interface HeadTrait<T> {
    @Nullable
    T headOrNull();

    default T head() {
        T head = headOrNull();
        if (head == null) {
            throw new NoSuchElementException("Can't get head from collection: it is empty");
        }
        return head;
    }
}
