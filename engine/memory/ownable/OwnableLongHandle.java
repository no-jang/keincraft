package engine.memory.ownable;

import engine.memory.handle.LongHandle;
import engine.memory.owner.Owner;

public abstract class OwnableLongHandle<P extends Owner<Ownable<P>>> extends OwnableBase<P> implements LongHandle {
    protected final long handle;

    public OwnableLongHandle(P owner, long handle) {
        super(owner);
        this.handle = handle;
    }

    @Override
    public long handle() {
        return handle;
    }
}
