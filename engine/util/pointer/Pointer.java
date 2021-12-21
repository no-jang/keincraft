package engine.util.pointer;

public abstract class Pointer {
    protected final long handle;

    public Pointer(long handle) {
        this.handle = handle;
    }

    public long getHandle() {
        return handle;
    }
}
