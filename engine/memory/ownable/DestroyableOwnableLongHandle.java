package engine.memory.ownable;

import engine.memory.handle.LongHandle;
import engine.memory.owner.Owner;

public abstract class DestroyableOwnableLongHandle<P extends Owner<Ownable<P>>> extends DestroyableOwnable<P> implements LongHandle {
    protected final long handle;

    public DestroyableOwnableLongHandle(P owner, long handle) {
        super(owner);
        this.handle = handle;
    }

    @Override
    public long handle() {
        throwIfDestroyed();
        return handle;
    }
}
