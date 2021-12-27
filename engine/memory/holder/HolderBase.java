package engine.memory.holder;

import java.util.ArrayList;
import java.util.List;

public abstract class HolderBase<R> implements Holder<R> {
    protected final List<R> resources;

    public HolderBase() {
        this.resources = new ArrayList<>();
    }

    @Override
    public List<R> getResources() {
        return resources;
    }
}
