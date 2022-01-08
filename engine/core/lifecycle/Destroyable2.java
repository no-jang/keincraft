package engine.core.lifecycle;

public interface Destroyable2 extends Destroyable {
    boolean isDestroyed();

    void doDestroy();

    @Override
    default void destroy() {
        if (isDestroyed()) {
            return;
        }

        doDestroy();
    }

    default void throwIfDestroyed() {
        if (isDestroyed()) {
            throw new IllegalStateException("Object can't be used: It is already destroyed");
        }
    }
}
