package engine.memory.rholder;

import engine.memory.holder.Holder;
import engine.memory.resource.DResourceBase;
import engine.memory.resource.Resource;

import java.util.ArrayList;
import java.util.List;

public abstract class DRHolderBase<H extends Holder<Resource<H>>, R extends Resource<Holder<R>>> extends DResourceBase<H> implements DRHolder<H, R> {
    protected final List<R> resources;

    public DRHolderBase(H holder) {
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
