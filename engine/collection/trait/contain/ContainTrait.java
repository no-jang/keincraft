package engine.collection.trait.contain;

import engine.util.Conditions;

public interface ContainTrait<T> {
    boolean contains(T element);

    default boolean containsAll(Iterable<T> iterable) {
        Conditions.argumentNotNull(iterable, "Can't check containment from collection: iterable is null");
        for (T element : iterable) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }
}
