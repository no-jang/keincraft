package engine.memory.owner;

import engine.memory.struct.Struct;

public abstract class AbstractDestroyableOwnerStruct<T> extends AbstractDestroyableOwnerContainer<T> implements Struct<T> {
    protected final long address;

    public AbstractDestroyableOwnerStruct(T reference, long address) {
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
