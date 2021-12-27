package engine.memory.resource;

public abstract class ResourceBase<H> implements Resource<H> {
    protected final H holder;

    public ResourceBase(H holder) {
        this.holder = holder;
    }

    @Override
    public H getHolder() {
        return holder;
    }
}
