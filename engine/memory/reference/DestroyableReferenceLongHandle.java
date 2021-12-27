package engine.memory.reference;

import engine.memory.handle.LongHandle;
import engine.memory.ownable.Ownable;
import engine.memory.owner.Owner;

public abstract class DestroyableReferenceLongHandle<P extends Owner<Ownable<P>>, C extends Ownable<? extends Owner<C>>> extends DestroyableReference<P, C> implements LongHandle {
    protected final long handle;

    public DestroyableReferenceLongHandle(P owner, long handle) {
        super(owner);
        this.handle = handle;
    }

    @Override
    public long handle() {
        throwIfDestroyed();
        return handle;
    }
}
