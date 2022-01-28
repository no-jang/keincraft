package engine.collection.trait;

import engine.util.Conditions;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface IndexTrait<T> {
    int indexOf(T element);

    @Nullable
    T getOrNull(int index);

    default T get(int index) {
        T element = getOrNull(index);
        Conditions.elementNotNull(element, "Can't get from list: index " + index + " is out of range");
        return element;
    }
}
