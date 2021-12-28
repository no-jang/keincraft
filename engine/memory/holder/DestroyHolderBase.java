package engine.memory.holder;

import engine.memory.destroy.DestroyBase;

import java.util.ArrayList;
import java.util.List;

public abstract class DestroyHolderBase<R> extends DestroyBase implements DestroyHolder<R> {
    protected final List<R> resources;

    public DestroyHolderBase() {
        this.resources = new ArrayList<>();
    }

    @Override
    public void destroy() {
        DestroyHolder.super.destroy();
        super.destroy();
    }

    @Override
    public List<R> getResources() {
        throwIfDestroyed();
        return resources;
    }
}
