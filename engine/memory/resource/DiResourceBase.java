package engine.memory.resource;

import java.util.ArrayList;
import java.util.List;

public abstract class DiResourceBase<H> implements DiResource<H> {
    protected final List<H> holders;

    public DiResourceBase() {
        this.holders = new ArrayList<>();
    }

    @Override
    public List<H> getHolders() {
        return holders;
    }
}
