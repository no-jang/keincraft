package engine.memory.owner;

import engine.memory.destroy.Destroyable;
import engine.memory.ownable.Ownable;

import java.util.List;

public abstract class AbstractDestroyableOwner extends AbstractOwner implements Destroyable {
    protected boolean isDestroyed;

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

        doDestroy();
        isDestroyed = true;
    }

    protected abstract void doDestroy();
}
