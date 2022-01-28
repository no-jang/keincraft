package engine.collection.trait;

import engine.util.Conditions;

public interface ContainIndexTrait<T> extends ContainTrait<T>, IndexTrait<T> {
    @Override
    default boolean contains(T element) {
        Conditions.argumentNotNull(element, "Can't check containment from collection: element is null");
        return indexOf(element) >= 0;
    }
}
