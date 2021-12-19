package engine.helper.pointer;

public abstract class ReferencePointer<T> extends Pointer {
    protected final T reference;

    public ReferencePointer(T reference, long handle) {
        super(handle);

        this.reference = reference;
    }

    public T getReference() {
        return reference;
    }
}
