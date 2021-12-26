package engine.memory.ownable;

import engine.memory.owner.Owner;
import engine.memory.struct.Struct;

public abstract class AbstractOwnableStruct<O extends Owner, T> extends AbstractOwnableContainer<O, T> implements Struct<T> {
    protected final long address;

    public AbstractOwnableStruct(O owner, T reference, long address) {
        super(owner, reference);
        this.address = address;
    }

    @Override
    public long address() {
        return address;
    }
}
