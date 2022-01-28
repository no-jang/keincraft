package engine.collection;

import engine.util.Conditions;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface OrderedCollection<T> extends Collection<T> {
    @Nullable
    T getHeadOrNull();

    default T getHead() {
        T head = getHeadOrNull();
        Conditions.elementNotNull(head, "Can not get head from collection: it is empty");
        return head;
    }
}
