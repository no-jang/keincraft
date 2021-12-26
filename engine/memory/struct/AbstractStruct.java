package engine.memory.struct;

import engine.memory.container.AbstractContainer;

public abstract class AbstractStruct<T> extends AbstractContainer<T> implements Struct<T> {
    protected final long address;

    public AbstractStruct(T reference, long address) {
        super(reference);
        this.address = address;
    }

    @Override
    public long address() {
        return address;
    }
}
