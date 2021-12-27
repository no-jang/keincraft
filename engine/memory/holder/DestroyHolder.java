package engine.memory.holder;

import engine.memory.destroy.Destroy;

import java.io.Closeable;
import java.io.IOException;

public interface DestroyHolder<R> extends Holder<R>, Destroy {
    @Override
    default void destroy() {
        for (R resource : getResources()) {
            if (resource instanceof Destroy) {
                ((Destroy) resource).close();
            } else if (resource instanceof Closeable) {
                try {
                    ((Closeable) resource).close();
                } catch (IOException e) {
                    throw new RuntimeException("Failed to close resource: ", e);
                }
            }
        }
    }
}
