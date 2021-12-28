package engine.memory.resource;

import engine.memory.destroy.DestroyBase;
import engine.memory.holder.Holder;

public abstract class DestroyResourceBase<H extends Holder<Resource<H>>> extends DestroyBase implements DestroyResource<H> {
    protected final H holder;

    public DestroyResourceBase(H holder) {
        this.holder = holder;
        this.holder.addResource(this);
    }

    @Override
    public H getHolder() {
        throwIfDestroyed();
        return holder;
    }
}
