package engine.memory.owner;

import engine.memory.struct.Struct;

public abstract class AbstractOwnerStruct<T> extends AbstractOwnerContainer<T> implements Struct<T> {
    protected final long address;

    public AbstractOwnerStruct(T reference, long address) {
        super(reference);
        this.address = address;
    }

    @Override
    public long address() {
        return address;
    }
}
