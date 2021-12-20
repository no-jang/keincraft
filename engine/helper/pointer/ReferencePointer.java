package engine.helper.pointer;

public abstract class ReferencePointer<T extends org.lwjgl.system.Pointer> extends Pointer {
    protected final T reference;

    public ReferencePointer(T reference) {
        super(reference.address());

        this.reference = reference;
    }

    public T getReference() {
        return reference;
    }
}
