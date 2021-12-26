package engine.memory.reference;

import engine.memory.owner.Owner;
import engine.memory.pointer.Pointer;

public abstract class AbstractDestroyableReferencePointer<T extends Owner> extends AbstractDestroyableReference<T> implements Pointer {
    protected final long address;

    public AbstractDestroyableReferencePointer(T owner, long address) {
        super(owner);
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
