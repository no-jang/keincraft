package engine.memory.destroy;

import java.io.Closeable;

public interface Destroy extends Closeable {
    boolean isDestroyed();

    void destroy();

    @Override
    default void close() {
        if (isDestroyed()) {
            return;
        }

        destroy();
    }

    default void throwIfDestroyed() {
        if (isDestroyed()) {
            throw new IllegalStateException("Object is destroyed and so can't be used");
        }
    }
}
