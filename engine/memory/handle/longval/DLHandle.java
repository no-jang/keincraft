package engine.memory.handle.longval;

import engine.memory.destroy.DestroyBase;

public abstract class DLHandle extends DestroyBase implements LHandle {
    protected long handle;

    public DLHandle(long handle) {
        this.handle = handle;
    }

    @Override
    public long getHandle() {
        throwIfDestroyed();
        return handle;
    }
}
