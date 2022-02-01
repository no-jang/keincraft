package engine.collection.trait.order;

import org.checkerframework.checker.nullness.qual.Nullable;

public interface HeadTrait<T> {
    @Nullable
    T headOrNull();

    default T head() {
        T head = headOrNull();

        if (head == null) {
            throw new NullPointerException("Can not peek first from collection: it is empty");
        }

        return head;
    }
}
