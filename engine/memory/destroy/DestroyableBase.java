package engine.memory.destroy;

public abstract class DestroyableBase implements Destroyable {
    protected boolean isDestroyed;

    @Override
    public void destroy() {
        if (isDestroyed) {
            return;
        }

        doDestroy();
        isDestroyed = true;
    }

    @Override
    public boolean isDestroyed() {
        return isDestroyed;
    }

    protected abstract void doDestroy();
}
