package engine.collection.iteration;

public interface MutableIterable<T> extends Iterable<T> {
    MutableIterator<T> iterator();
}
