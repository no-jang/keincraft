package engine.memory.handle.longval;

import engine.memory.destroy.DestroyBase;

public abstract class DestroyLongHandleBase extends DestroyBase implements LongHandle {
    protected long handle;

    public DestroyLongHandleBase(long handle) {
        this.handle = handle;
    }

    @Override
    public long getHandle() {
        throwIfDestroyed();
        return handle;
    }
}
