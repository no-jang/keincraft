package engine.memory.handle.longval;

import engine.memory.holder.Holder;
import engine.memory.resource.DResourceBase;
import engine.memory.resource.Resource;

public abstract class DResourceLHandle<H extends Holder<Resource<H>>> extends DResourceBase<H> implements LHandle {
    protected final long handle;

    public DResourceLHandle(H holder, long handle) {
        super(holder);
        this.handle = handle;
    }

    @Override
    public long getHandle() {
        throwIfDestroyed();
        return handle;
    }
}
