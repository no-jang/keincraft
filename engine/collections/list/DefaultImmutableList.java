package engine.collections.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class DefaultImmutableList<E> implements ImmutableList<E> {
    private final List<E> list;

    public DefaultImmutableList(List<E> list) {
        this.list = list;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(E object) {
        return list.contains(object);
    }

    @Override
    public boolean containsAll(Collection<E> collection) {
        return list.containsAll(collection);
    }

    @Override
    public int indexOf(E object) {
        return list.indexOf(object);
    }

    @Override
    public int lastIndexOf(E object) {
        return list.lastIndexOf(object);
    }

    @Override
    public E get(int index) {
        return list.get(index);
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public List<E> toMutable() {
        return list;
    }
}
