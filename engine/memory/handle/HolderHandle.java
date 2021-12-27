package engine.memory.handle;

import engine.memory.holder.HolderBase;

public abstract class HolderHandle<R, T> extends HolderBase<R> implements Handle<T> {
    protected final T handle;

    public HolderHandle(T handle) {
        this.handle = handle;
    }

    @Override
    public T getHandle() {
        return handle;
    }
}
