package engine.memory.handle.longval;

import engine.memory.holder.DHolderBase;

public abstract class DHolderLHandle<R> extends DHolderBase<R> implements LHandle {
    protected final long handle;

    public DHolderLHandle(long handle) {
        this.handle = handle;
    }

    @Override
    public long getHandle() {
        throwIfDestroyed();
        return handle;
    }
}
