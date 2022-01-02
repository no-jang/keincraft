package engine.collections.container;

import java.util.Collection;
import java.util.List;

public interface Container<R> {
    List<R> getAvailable();

    List<R> getRequested();

    interface Builder<R> {
        Builder<R> required(R request);

        Builder<R> required(Collection<R> requests);

        Builder<R> optional(R request);

        Builder<R> optional(Collection<R> requests);

        Container<R> build();
    }
}
