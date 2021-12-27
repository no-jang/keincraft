package engine.memory.destroy;

import engine.memory.handle.Handle;

public abstract class DestroyableHandle<T> extends DestroyableBase implements Handle<T> {
    protected final T handle;

    public DestroyableHandle(T handle) {
        this.handle = handle;
    }

    @Override
    public T handle() {
        throwIfDestroyed();
        return handle;
    }
}
