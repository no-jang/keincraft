package engine.memory.handle;

import engine.memory.holder.Holder;
import engine.memory.resource.Resource;
import engine.memory.rholder.RHolderBase;

public abstract class RHolderHandle<H extends Holder<Resource<H>>, R, T> extends RHolderBase<H, R> implements Handle<T> {
    protected final T handle;

    public RHolderHandle(H holder, T handle) {
        super(holder);
        this.handle = handle;
    }

    @Override
    public T getHandle() {
        return handle;
    }
}
