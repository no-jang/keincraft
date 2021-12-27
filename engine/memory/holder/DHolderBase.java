package engine.memory.holder;

import engine.memory.destroy.DestroyBase;

import java.util.ArrayList;
import java.util.List;

public abstract class DHolderBase<R> extends DestroyBase implements DHolder<R> {
    protected final List<R> resources;

    public DHolderBase() {
        this.resources = new ArrayList<>();
    }

    @Override
    public List<R> getResources() {
        throwIfDestroyed();
        return resources;
    }
}
