package engine.memory.ownable;

import engine.memory.handle.Handle;
import engine.memory.owner.Owner;

public abstract class DestroyableOwnableHandle<P extends Owner<Ownable<P>>, T> extends DestroyableOwnable<P> implements Handle<T> {
    protected final T handle;

    public DestroyableOwnableHandle(P owner, T handle) {
        super(owner);
        this.handle = handle;
    }

    @Override
    public T handle() {
        throwIfDestroyed();
        return handle;
    }
}
