package engine.memory.handle;

import engine.memory.destroy.DestroyBase;

public abstract class DestroyHandleBase<T> extends DestroyBase implements Handle<T> {
    protected T handle;

    public DestroyHandleBase(T handle) {
        this.handle = handle;
    }

    @Override
    public T getHandle() {
        throwIfDestroyed();
        return handle;
    }
}
