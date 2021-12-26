package engine.memory.reference;

import engine.memory.destroy.Destroyable;
import engine.memory.ownable.Ownable;
import engine.memory.owner.Owner;

import java.util.List;

public abstract class AbstractDestroyableReference<T extends Owner> extends AbstractReference<T> implements Destroyable {
    protected boolean isDestroyed;

    public AbstractDestroyableReference(T owner) {
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
    public List<Ownable<?>> owning() {
        if (isDestroyed) {
            throw new IllegalStateException("Object is already destroyed");
        }

        return super.owning();
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

        owner.owning().remove(this);
        doDestroy();
        isDestroyed = true;
    }

    protected abstract void doDestroy();
}
