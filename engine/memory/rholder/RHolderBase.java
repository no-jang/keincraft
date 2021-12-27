package engine.memory.rholder;

import engine.memory.holder.Holder;
import engine.memory.resource.Resource;
import engine.memory.resource.ResourceBase;

import java.util.ArrayList;
import java.util.List;

public abstract class RHolderBase<H extends Holder<Resource<H>>, R> extends ResourceBase<H> implements RHolder<H, R> {
    protected final List<R> resources;

    public RHolderBase(H holder) {
        super(holder);
        this.resources = new ArrayList<>();
    }

    @Override
    public List<R> getResources() {
        return resources;
    }
}
