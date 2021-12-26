package engine.memory.destroy;

import engine.memory.pointer.Pointer;

public abstract class AbstractDestroyableStruct<T> extends AbstractDestroyableContainer<T> implements Pointer {
    protected final long address;

    public AbstractDestroyableStruct(T reference, long address) {
        super(reference);
        this.address = address;
    }

    @Override
    public long address() {
        if (isDestroyed) {
            throw new IllegalStateException("Object is already destroyed");
        }

        return address;
    }
}
