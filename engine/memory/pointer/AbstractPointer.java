package engine.memory.pointer;

public abstract class AbstractPointer implements Pointer {
    protected final long address;

    public AbstractPointer(long address) {
        this.address = address;
    }

    @Override
    public long address() {
        return address;
    }
}
