package engine.memory.handle;

import engine.memory.destroy.DestroyBase;

public abstract class DHandle<T> extends DestroyBase implements Handle<T> {
    protected T handle;

    public DHandle(T handle) {
        this.handle = handle;
    }

    @Override
    public T getHandle() {
        throwIfDestroyed();
        return handle;
    }
}
