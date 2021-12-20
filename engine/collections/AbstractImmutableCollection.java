package engine.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class AbstractImmutableCollection<C extends Collection<E>, E> implements ImmutableCollection<E> {
    protected final C wrapped;

    public AbstractImmutableCollection(C wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public int size() {
        return wrapped.size();
    }

    @Override
    public boolean isEmpty() {
        return wrapped.isEmpty();
    }

    @Override
    public boolean contains(E object) {
        return wrapped.contains(object);
    }

    @Override
    public boolean containsAll(Collection<E> collection) {
        return wrapped.containsAll(collection);
    }

    @Override
    public C toMutable() {
        return wrapped;
    }

    @Override
    public Stream<E> stream() {
        return wrapped.stream();
    }

    @Override
    public Iterator<E> iterator() {
        return wrapped.iterator();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        wrapped.forEach(action);
    }

    @Override
    public Spliterator<E> spliterator() {
        return wrapped.spliterator();
    }

    @Override
    public String toString() {
        return wrapped.toString();
    }
}
