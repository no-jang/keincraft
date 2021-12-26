package engine.memory.owner;

import engine.memory.ownable.Ownable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractOwner implements Owner {
    protected final List<Ownable<?>> owning;

    public AbstractOwner() {
        this.owning = new ArrayList<>();
    }

    @Override
    public List<Ownable<?>> owning() {
        return owning;
    }
}
