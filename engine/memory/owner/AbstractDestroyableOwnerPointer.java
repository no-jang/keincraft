package engine.memory.owner;

import engine.memory.pointer.Pointer;

public abstract class AbstractDestroyableOwnerPointer extends AbstractDestroyableOwner implements Pointer {
    protected final long address;

    public AbstractDestroyableOwnerPointer(long address) {
        super();
        this.address = address;
    }

    @Override
    public long address() {
        if (isDestroyed) {
            throw new IllegalStateException("Object already destroyed");
        }

        return address;
    }
}
