package engine.collections.container;

import java.util.Collection;
import java.util.List;

public interface DoubleContainer<A, R> {
    List<A> getAvailable();

    List<R> getRequested();

    interface Builder<A, R> {
        Builder<A, R> required(R request);

        Builder<A, R> required(Collection<R> requests);

        Builder<A, R> optional(R request);

        Builder<A, R> optional(Collection<R> requests);

        DoubleContainer<A, R> build();
    }
}
