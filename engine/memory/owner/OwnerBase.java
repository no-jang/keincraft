package engine.memory.owner;

import engine.memory.ownable.Ownable;

import java.util.ArrayList;
import java.util.List;

public abstract class OwnerBase<C extends Ownable<? extends Owner<C>>> implements Owner<C> {
    protected final List<C> owning;

    public OwnerBase() {
        this.owning = new ArrayList<>();
    }

    @Override
    public List<C> owning() {
        return owning;
    }
}
