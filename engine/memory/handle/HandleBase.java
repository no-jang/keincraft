package engine.memory.handle;

public abstract class HandleBase<T> implements Handle<T> {
    private final T handle;

    public HandleBase(T handle) {
        this.handle = handle;
    }

    @Override
    public T getHandle() {
        return handle;
    }
}
