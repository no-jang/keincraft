package engine.memory.handle.longval;

import engine.memory.holder.HolderBase;

public abstract class HolderLHandle<R> extends HolderBase<R> implements LHandle {
    protected final long handle;

    public HolderLHandle(long handle) {
        this.handle = handle;
    }

    @Override
    public long getHandle() {
        return handle;
    }
}
