package engine.memory.destroy;

import engine.memory.handle.LongHandle;

public abstract class DestroyableLongHandle extends DestroyableBase implements LongHandle {
    protected final long handle;

    public DestroyableLongHandle(long handle) {
        this.handle = handle;
    }

    @Override
    public long handle() {
        throwIfDestroyed();
        return handle;
    }
}
