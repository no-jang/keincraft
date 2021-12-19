package engine.collections;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;

public interface ImmutableCollection<E> extends Iterable<E> {
    int size();
    boolean isEmpty();

    boolean contains(E object);
    boolean containsAll(Collection<E> collection);

    Collection<E> toMutable();
}
