package engine.memory.destroy;

public abstract class DestroyBase implements Destroy {
    protected boolean isDestroyed;

    @Override
    public boolean isDestroyed() {
        return isDestroyed;
    }

    @Override
    public void destroy() {
        isDestroyed = true;
    }
}
