package engine.memory.handle;

import engine.memory.holder.Holder;
import engine.memory.resource.Resource;
import engine.memory.rholder.DRHolderBase;

public abstract class DRHolderHandle<H extends Holder<Resource<H>>, R extends Resource<Holder<R>>, T> extends DRHolderBase<H, R> implements Handle<T> {
    protected final T handle;

    public DRHolderHandle(H holder, T handle) {
        super(holder);
        this.handle = handle;
    }

    @Override
    public T getHandle() {
        throwIfDestroyed();
        return handle;
    }
}
