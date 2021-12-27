package engine.memory.reference;

import engine.memory.handle.Handle;
import engine.memory.ownable.Ownable;
import engine.memory.owner.Owner;

public abstract class ReferenceHandle<P extends Owner<Ownable<P>>, C extends Ownable<? extends Owner<C>>, T> extends ReferenceBase<P, C> implements Handle<T> {
    protected final T handle;

    public ReferenceHandle(P owner, T handle) {
        super(owner);
        this.handle = handle;
    }

    @Override
    public T handle() {
        return handle;
    }
}
