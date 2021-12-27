package engine.memory.handle;

import engine.memory.holder.Holder;
import engine.memory.resource.DResourceBase;
import engine.memory.resource.Resource;

public abstract class DResourceHandle<H extends Holder<Resource<H>>, T> extends DResourceBase<H> implements Handle<T> {
    protected final T handle;

    public DResourceHandle(H holder, T handle) {
        super(holder);
        this.handle = handle;
    }

    @Override
    public T getHandle() {
        throwIfDestroyed();
        return handle;
    }
}
