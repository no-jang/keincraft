package engine.memory.reference;

import engine.memory.destroy.Destroyable;
import engine.memory.ownable.Ownable;
import engine.memory.owner.Owner;

public abstract class DestroyableReference<P extends Owner<Ownable<P>>, C extends Ownable<? extends Owner<C>>> extends ReferenceBase<P, C> implements Destroyable {
    protected boolean isDestroyed;

    public DestroyableReference(P owner) {
        super(owner);
    }

    @Override
    public void destroy() {
        if (isDestroyed) {
            return;
        }

        for (Ownable<?> ownable : owning) {
            if (ownable instanceof Destroyable) {
                ((Destroyable) ownable).destroy();
            }
        }

        doDestroy();
        owner.owning().remove(this);
        isDestroyed = true;
    }

    @Override
    public boolean isDestroyed() {
        return isDestroyed;
    }

    protected abstract void doDestroy();
}
