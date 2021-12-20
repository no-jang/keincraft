package engine.collections.list;

import engine.collections.AbstractImmutableCollection;

import java.util.List;

public class DefaultImmutableList<E> extends AbstractImmutableCollection<List<E>, E> implements ImmutableList<E> {
    public DefaultImmutableList(List<E> wrapped) {
        super(wrapped);
    }

    @Override
    public int indexOf(E object) {
        return wrapped.indexOf(object);
    }

    @Override
    public int lastIndexOf(E object) {
        return wrapped.lastIndexOf(object);
    }

    @Override
    public E get(int index) {
        return wrapped.get(index);
    }
}
