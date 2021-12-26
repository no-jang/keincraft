package engine.memory.reference;

import engine.memory.owner.Owner;
import engine.memory.pointer.Pointer;

public abstract class AbstractReferencePointer<T extends Owner> extends AbstractReference<T> implements Pointer {
    protected final long address;

    public AbstractReferencePointer(T owner, long address) {
        super(owner);
        this.address = address;
    }

    @Override
    public long address() {
        return address;
    }
}
