package engine.util.pointer;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class DestroyablePointer extends Pointer implements Destroyable {
    private final AtomicBoolean isDisposed;

    public DestroyablePointer(long handle) {
        super(handle);

        isDisposed = new AtomicBoolean(false);
    }

    @Override
    public long getHandle() {
        if (isDisposed.get()) {
            throw new IllegalStateException("Underlying pointer has already been destroyed");
        }

        return super.getHandle();
    }

    @Override
    public void destroy() {
        if (!isDisposed.getAndSet(true)) {
            destroy(handle);
        }
    }

    protected abstract void destroy(long handle);
}
