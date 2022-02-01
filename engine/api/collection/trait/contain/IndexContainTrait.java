package engine.api.collection.trait.contain;

import engine.api.collection.trait.index.IndexTrait;
import engine.common.util.Conditions;

public interface IndexContainTrait<T> extends ContainTrait<T>, IndexTrait<T> {
    @Override
    default boolean contains(T element) {
        Conditions.argumentNotNull(element, "Can't check containment from collection: element is null");
        return indexOf(element) >= 0;
    }
}
