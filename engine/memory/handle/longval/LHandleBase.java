package engine.memory.handle.longval;

public abstract class LHandleBase implements LHandle {
    protected final long handle;

    public LHandleBase(long handle) {
        this.handle = handle;
    }

    @Override
    public long getHandle() {
        return handle;
    }
}
