package engine.memory.handle;

import engine.memory.holder.Holder;
import engine.memory.resource.Resource;
import engine.memory.resource.ResourceBase;

public abstract class ResourceHandle<H extends Holder<Resource<H>>, T> extends ResourceBase<H> implements Handle<T> {
    protected T handle;

    public ResourceHandle(H holder, T handle) {
        super(holder);
        this.handle = handle;
    }

    @Override
    public T getHandle() {
        return handle;
    }
}
