package engine.memory.destroy;

import engine.memory.pointer.Pointer;

public abstract class AbstractDestroyablePointer extends AbstractDestroyable implements Pointer {
    protected final long address;

    public AbstractDestroyablePointer(long address) {
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
