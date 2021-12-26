package engine.memory.reference;

import engine.memory.owner.Owner;
import engine.memory.struct.Struct;

public abstract class AbstractReferenceStruct<O extends Owner, T> extends AbstractReferenceContainer<O, T> implements Struct<T> {
    protected final long address;

    public AbstractReferenceStruct(O owner, T reference, long address) {
        super(owner, reference);
        this.address = address;
    }

    @Override
    public long address() {
        return address;
    }
}
