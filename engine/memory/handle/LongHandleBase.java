package engine.memory.handle;

public abstract class LongHandleBase implements LongHandle {
    protected final long handle;

    public LongHandleBase(long handle) {
        this.handle = handle;
    }

    @Override
    public long handle() {
        return handle;
    }
}
