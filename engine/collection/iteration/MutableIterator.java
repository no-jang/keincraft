package engine.collection.iteration;

public interface MutableIterator<T> extends Iterator<T> {
    @Override
    void remove();
}
