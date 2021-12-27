package engine.memory.ownable;

import engine.memory.handle.Handle;
import engine.memory.owner.Owner;

public abstract class OwnableHandle<P extends Owner<Ownable<P>>, T> extends OwnableBase<P> implements Handle<T> {
    protected final T handle;

    public OwnableHandle(P owner, T handle) {
        super(owner);
        this.handle = handle;
    }

    @Override
    public T handle() {
        return handle;
    }
}
