package client.graphics.vk.models.pointers;

public abstract class Pointer {
    protected abstract long getInternalHandle();

    public long getHandle() {
        return getInternalHandle();
    }
}
