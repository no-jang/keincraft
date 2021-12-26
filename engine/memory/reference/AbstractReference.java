package engine.memory.reference;

import engine.memory.ownable.AbstractOwnable;
import engine.memory.ownable.Ownable;
import engine.memory.owner.Owner;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractReference<T extends Owner> extends AbstractOwnable<T> implements Owner {
    protected final List<Ownable<?>> owning;

    public AbstractReference(T owner) {
        super(owner);
        this.owning = new ArrayList<>();
    }

    @Override
    public List<Ownable<?>> owning() {
        return owning;
    }
}
