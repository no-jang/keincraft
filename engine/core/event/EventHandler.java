package engine.core.event;

@FunctionalInterface
public interface EventHandler<E extends Event> {
    void handle(E e);
}
