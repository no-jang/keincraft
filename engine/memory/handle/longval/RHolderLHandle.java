package engine.memory.handle.longval;

import engine.memory.holder.Holder;
import engine.memory.resource.Resource;
import engine.memory.rholder.RHolderBase;

public abstract class RHolderLHandle<H extends Holder<Resource<H>>, R> extends RHolderBase<H, R> implements LHandle {
    protected final long handle;

    public RHolderLHandle(H holder, long handle) {
        super(holder);
        this.handle = handle;
    }

    @Override
    public long getHandle() {
        return handle;
    }
}
