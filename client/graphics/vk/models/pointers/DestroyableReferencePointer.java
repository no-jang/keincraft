package client.graphics.vk.models.pointers;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class DestroyableReferencePointer<T> extends ReferencePointer<T> implements Destroyable {
    private final AtomicBoolean isDisposed;

    protected DestroyableReferencePointer() {
        isDisposed = new AtomicBoolean(false);
    }

    @Override
    public long getHandle() {
        if (isDisposed.get()) {
            throw new IllegalStateException("Underlying instance has already been destroyed");
        }

        return super.getHandle();
    }

    @Override
    public T getReference() {
        if (isDisposed.get()) {
            throw new IllegalStateException("Underlying instance has already been destroyed");
        }

        return super.getReference();
    }

    @Override
    public void destroy() {
        if (!isDisposed.getAndSet(true)) {
            internalDestroy();
        }
    }

    protected abstract void internalDestroy();
}
