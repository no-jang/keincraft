package engine.memory.owner;

import engine.memory.destroy.Destroyable;
import engine.memory.ownable.Ownable;

import java.util.List;

public abstract class DestroyableOwner<C extends Ownable<? extends Owner<C>>> extends OwnerBase<C> implements Destroyable {
    protected boolean isDestroyed;

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
        isDestroyed = true;
    }

    @Override
    public List<C> owning() {
        throwIfDestroyed();
        return super.owning();
    }

    @Override
    public boolean isDestroyed() {
        return isDestroyed;
    }

    protected abstract void doDestroy();
}
