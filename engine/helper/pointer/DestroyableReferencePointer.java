package engine.helper.pointer;

import org.lwjgl.system.Pointer;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class DestroyableReferencePointer<T extends Pointer> extends ReferencePointer<T> implements Destroyable {
    private final AtomicBoolean isDestroyed;

    public DestroyableReferencePointer(T reference) {
        super(reference);

        isDestroyed = new AtomicBoolean(false);
    }

    @Override
    public long getHandle() {
        if (isDestroyed.get()) {
            throw new IllegalStateException("Underlying reference has already been destroyed");
        }

        return super.getHandle();
    }

    @Override
    public T getReference() {
        if (isDestroyed.get()) {
            throw new IllegalStateException("Underlying reference has already been destroyed");
        }

        return super.getReference();
    }

    @Override
    public void destroy() {
        if (!isDestroyed.getAndSet(true)) {
            destroy(reference);
        }
    }

    protected abstract void destroy(T reference);
}
