package engine.memory.reference;

import engine.memory.ownable.Ownable;
import engine.memory.owner.Owner;
import engine.memory.owner.OwnerBase;

public abstract class ReferenceBase<P extends Owner<Ownable<P>>, C extends Ownable<? extends Owner<C>>> extends OwnerBase<C> implements Reference<P, C> {
    protected final P owner;

    public ReferenceBase(P owner) {
        this.owner = owner;
        this.owner.owning().add(this);
    }

    @Override
    public P owner() {
        return owner;
    }
}
