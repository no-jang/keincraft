package engine.memory.container;

public abstract class AbstractContainer<T> implements Container<T> {
    protected final T reference;

    public AbstractContainer(T reference) {
        this.reference = reference;
    }

    @Override
    public T unwrap() {
        return reference;
    }
}
