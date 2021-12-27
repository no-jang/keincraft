package engine.memory.handle.longval;

import engine.memory.holder.Holder;
import engine.memory.resource.Resource;
import engine.memory.resource.ResourceBase;

public abstract class ResourceLHandle<H extends Holder<Resource<H>>> extends ResourceBase<H> implements LHandle {
    protected long handle;

    public ResourceLHandle(H holder, long handle) {
        super(holder);
        this.handle = handle;
    }

    @Override
    public long getHandle() {
        return handle;
    }
}
