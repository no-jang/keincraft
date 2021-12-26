package engine.memory.ownable;

import engine.memory.owner.Owner;

public abstract class AbstractOwnable<T extends Owner> implements Ownable<T> {
    protected final T owner;

    public AbstractOwnable(T owner) {
        this.owner = owner;
        owner.owning().add(this);
    }

    @Override
    public T owner() {
        return owner;
    }
}
