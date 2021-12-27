package engine.memory.owner;

import engine.memory.handle.Handle;
import engine.memory.ownable.Ownable;

public abstract class OwnerHandle<C extends Ownable<? extends Owner<C>>, T> extends OwnerBase<C> implements Handle<T> {
    protected final T handle;

    public OwnerHandle(T handle) {
        this.handle = handle;
    }

    @Override
    public T handle() {
        return handle;
    }
}
