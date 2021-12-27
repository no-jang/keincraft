package engine.memory.owner;

import engine.memory.handle.LongHandle;
import engine.memory.ownable.Ownable;

public abstract class DestroyableOwnerLongHandle<C extends Ownable<? extends Owner<C>>> extends DestroyableOwner<C> implements LongHandle {
    protected final long handle;

    public DestroyableOwnerLongHandle(long handle) {
        this.handle = handle;
    }

    @Override
    public long handle() {
        throwIfDestroyed();
        return handle;
    }
}
