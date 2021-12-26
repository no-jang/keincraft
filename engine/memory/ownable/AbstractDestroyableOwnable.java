package engine.memory.ownable;

import engine.memory.destroy.Destroyable;
import engine.memory.owner.Owner;

public abstract class AbstractDestroyableOwnable<T extends Owner> extends AbstractOwnable<T> implements Destroyable {
    protected boolean isDestroyed;

    public AbstractDestroyableOwnable(T owner) {
        super(owner);
    }

    @Override
    public T owner() {
        if (isDestroyed) {
            throw new IllegalStateException("Object is already destroyed");
        }

        return super.owner();
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

    protected abstract void doDestroy();
}
