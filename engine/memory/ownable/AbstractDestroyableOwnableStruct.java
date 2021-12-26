package engine.memory.ownable;

import engine.memory.owner.Owner;
import engine.memory.struct.Struct;

public abstract class AbstractDestroyableOwnableStruct<O extends Owner, T> extends AbstractDestroyableOwnableContainer<O, T> implements Struct<T> {
    protected final long address;

    public AbstractDestroyableOwnableStruct(O owner, T reference, long address) {
        super(owner, reference);
        this.address = address;
    }

    @Override
    public long address() {
        if (isDestroyed) {
            throw new IllegalStateException("Object is already destroyed");
        }

        return 0;
    }
}
