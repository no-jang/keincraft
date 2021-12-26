package engine.memory.destroy;

import java.io.Closeable;
import java.io.IOException;

public interface Destroyable extends Closeable {
    void destroy();

    @Override
    default void close() throws IOException {
        destroy();
    }
}
