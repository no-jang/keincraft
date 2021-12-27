package engine.memory.owner;

import engine.memory.handle.LongHandle;
import engine.memory.ownable.Ownable;

public abstract class OwnerLongHandle<C extends Ownable<? extends Owner<C>>> extends OwnerBase<C> implements LongHandle {
    protected final long handle;

    public OwnerLongHandle(long handle) {
        this.handle = handle;
    }

    @Override
    public long handle() {
        return handle;
    }
}
