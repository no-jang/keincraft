package engine.core.lifecycle;

import java.io.Closeable;

public interface Destroyable extends Closeable {
    void destroy();

    @Override
    default void close() {
        destroy();
    }
}
