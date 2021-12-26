package engine.memory.ownable;

import engine.memory.owner.Owner;
import engine.memory.pointer.Pointer;

public abstract class AbstractOwnablePointer<T extends Owner> extends AbstractOwnable<T> implements Pointer {
    protected final long address;

    public AbstractOwnablePointer(T owner, long address) {
        super(owner);
        this.address = address;
    }

    @Override
    public long address() {
        return address;
    }
}
