package engine.memory.ownable;

import engine.memory.destroy.Destroyable;
import engine.memory.owner.Owner;

public abstract class DestroyableOwnable<P extends Owner<Ownable<P>>> extends OwnableBase<P> implements Destroyable {
    protected boolean isDestroyed;

    public DestroyableOwnable(P owner) {
        super(owner);
    }

    @Override
    public void destroy() {
        if (isDestroyed) {
            return;
        }

        doDestroy();
        owner.owning().remove(this);
        isDestroyed = true;
    }

    @Override
    public P owner() {
        throwIfDestroyed();
        return super.owner();
    }

    @Override
    public boolean isDestroyed() {
        return isDestroyed;
    }

    protected abstract void doDestroy();
}
