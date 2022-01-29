package engine.collection.trait;

import engine.util.Conditions;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface TailTrait<T> {
    @Nullable
    T tailOrNull();

    default T getTail() {
        T tail = tailOrNull();
        Conditions.elementNotNull(tail, "Can't get tail from collection: it is empty");
        return tail;
    }
}
