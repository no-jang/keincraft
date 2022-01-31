package engine.collection.trait.mutable;

import engine.util.Conditions;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface StackTrait<T> {
    void push(T element);

    @Nullable
    T popOrNull();

    default void pushAll(Iterable<T> iterable) {
        Conditions.argumentNotNull(iterable, "Can't push onto collection: iterable is null");
        for (T element : iterable) {
            push(element);
        }
    }

    default T pop() {
        T element = popOrNull();
        Conditions.elementNotNull(element, "Can't pop from collection: it is empty");
        return element;
    }
}
