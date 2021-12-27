package engine.memory.resource;

import engine.memory.holder.Holder;

public abstract class ResourceBase<H extends Holder<Resource<H>>> implements Resource<H> {
    protected final H holder;

    public ResourceBase(H holder) {
        this.holder = holder;
        this.holder.addResource(this);
    }

    @Override
    public H getHolder() {
        return holder;
    }
}
