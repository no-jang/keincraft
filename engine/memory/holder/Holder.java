package engine.memory.holder;

import java.util.List;

public interface Holder<R> {
    List<R> getResources();

    default void addResource(R resource) {
        getResources().add(resource);
    }

    default void removeResource(R resource) {
        getResources().remove(resource);
    }
}
