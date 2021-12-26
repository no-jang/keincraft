package engine.memory.ownable;

import engine.memory.owner.Owner;
import engine.memory.pointer.Pointer;

public abstract class AbstractDestroyableOwnablePointer<T extends Owner> extends AbstractDestroyableOwnable<T> implements Pointer {
    protected final long address;

    public AbstractDestroyableOwnablePointer(T owner, long address) {
        super(owner);
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
