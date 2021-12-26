package engine.memory.owner;

import engine.memory.pointer.Pointer;

public abstract class AbstractOwnerPointer extends AbstractOwner implements Pointer {
    protected final long address;

    public AbstractOwnerPointer(long address) {
        super();
        this.address = address;
    }

    @Override
    public long address() {
        return address;
    }
}
