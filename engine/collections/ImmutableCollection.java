package engine.collections;

import java.util.Collection;
import java.util.stream.Stream;

public interface ImmutableCollection<E> extends Iterable<E> {
    int size();

    boolean isEmpty();

    boolean contains(E object);

    boolean containsAll(Collection<E> collection);

    Collection<E> toMutable();

    Stream<E> stream();
}
