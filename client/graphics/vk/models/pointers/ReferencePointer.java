package client.graphics.vk.models.pointers;

public abstract class ReferencePointer<T> extends Pointer implements Container<T> {
    @Override
    public T getReference() {
        return getInternalReference();
    }

    protected abstract T getInternalReference();
}
