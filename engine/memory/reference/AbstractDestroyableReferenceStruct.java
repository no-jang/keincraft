package engine.memory.reference;

import engine.memory.owner.Owner;
import engine.memory.struct.Struct;

public abstract class AbstractDestroyableReferenceStruct<O extends Owner, T> extends AbstractDestroyableReferenceContainer<O, T> implements Struct<T> {
    protected final long address;

    public AbstractDestroyableReferenceStruct(O owner, T reference, long address) {
        super(owner, reference);
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
