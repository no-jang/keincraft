package engine.memory.handle;

import engine.memory.holder.DHolderBase;

public abstract class DHolderHandle<R, T> extends DHolderBase<R> implements Handle<T> {
    protected final T handle;

    public DHolderHandle(T handle) {
        this.handle = handle;
    }

    @Override
    public T getHandle() {
        throwIfDestroyed();
        return handle;
    }
}
