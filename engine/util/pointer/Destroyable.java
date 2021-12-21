package engine.util.pointer;

import java.io.Closeable;
import java.io.IOException;

public interface Destroyable extends Closeable {
    void destroy();

    @Override
    default void close() throws IOException {
        destroy();
    }
}
