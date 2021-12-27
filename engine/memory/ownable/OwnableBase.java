package engine.memory.ownable;

import engine.memory.owner.Owner;

public abstract class OwnableBase<P extends Owner<Ownable<P>>> implements Ownable<P> {
    protected final P owner;

    public OwnableBase(P owner) {
        this.owner = owner;
        this.owner.owning().add(this);
    }

    @Override
    public P owner() {
        return owner;
    }
}
