package engine.memory.resource;

import engine.memory.destroy.DestroyBase;
import engine.memory.holder.Holder;

import java.util.ArrayList;
import java.util.List;

public abstract class DestroyDiResourceBase<H extends Holder<Resource<H>>> extends DestroyBase implements DestroyDiResource<H> {
    protected final List<H> holders;

    public DestroyDiResourceBase() {
        this.holders = new ArrayList<>();
    }

    @Override
    public List<H> getHolders() {
        throwIfDestroyed();
        return holders;
    }
}
