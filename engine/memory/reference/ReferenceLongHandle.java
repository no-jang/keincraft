package engine.memory.reference;

import engine.memory.handle.LongHandle;
import engine.memory.ownable.Ownable;
import engine.memory.owner.Owner;

public abstract class ReferenceLongHandle<P extends Owner<Ownable<P>>, C extends Ownable<? extends Owner<C>>> extends ReferenceBase<P, C> implements LongHandle {
    protected final long handle;

    public ReferenceLongHandle(P owner, long handle) {
        super(owner);
        this.handle = handle;
    }

    @Override
    public long handle() {
        return handle;
    }
}
