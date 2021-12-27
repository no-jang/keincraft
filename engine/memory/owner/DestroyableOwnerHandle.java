package engine.memory.owner;

import engine.memory.handle.Handle;
import engine.memory.ownable.Ownable;

public abstract class DestroyableOwnerHandle<C extends Ownable<? extends Owner<C>>, T> extends DestroyableOwner<C> implements Handle<T> {
    protected final T handle;

    public DestroyableOwnerHandle(T handle) {
        this.handle = handle;
    }

    @Override
    public T handle() {
        throwIfDestroyed();
        return handle;
    }
}
