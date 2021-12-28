package engine.memory.rholder;

import engine.memory.holder.Holder;
import engine.memory.resource.DestroyResourceBase;
import engine.memory.resource.Resource;

import java.util.ArrayList;
import java.util.List;

public abstract class DestroyResourceHolderBase<H extends Holder<Resource<H>>, R extends Resource<Holder<R>>> extends DestroyResourceBase<H> implements DestroyResourceHolder<H, R> {
    protected final List<R> resources;

    public DestroyResourceHolderBase(H holder) {
        super(holder);
        this.resources = new ArrayList<>();
    }

    @Override
    public List<R> getResources() {
        throwIfDestroyed();
        return resources;
    }

    @Override
    public H getHolder() {
        throwIfDestroyed();
        return super.getHolder();
    }
}
