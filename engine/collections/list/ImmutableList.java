package engine.collections.list;

import engine.collections.ImmutableCollection;

import java.util.List;

public interface ImmutableList<E> extends ImmutableCollection<E> {
    int indexOf(E object);

    int lastIndexOf(E object);

    E get(int index);

    List<E> toMutable();
}
