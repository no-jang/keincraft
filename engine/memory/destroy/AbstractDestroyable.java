package engine.memory.destroy;

public abstract class AbstractDestroyable implements Destroyable {
    protected boolean isDestroyed;

    @Override
    public void destroy() {
        if (isDestroyed) {
            return;
        }

        doDestroy();
        isDestroyed = true;
    }

    protected abstract void doDestroy();
}
