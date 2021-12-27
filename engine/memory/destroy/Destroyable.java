package engine.memory.destroy;

import java.io.Closeable;
import java.io.IOException;

public interface Destroyable extends Closeable {
    void destroy();

    boolean isDestroyed();

    default void throwIfDestroyed() {
        if (isDestroyed()) {
            throw new IllegalStateException("Object is already destroyed");
        }
    }

    @Override
    default void close() throws IOException {
        destroy();
    }
}
