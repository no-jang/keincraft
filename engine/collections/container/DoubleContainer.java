package engine.collections.container;

import engine.collections.list.ImmutableList;

import java.util.Collection;

public interface DoubleContainer<A, R> {
    ImmutableList<A> getAvailable();

    ImmutableList<R> getRequested();

    interface Builder<A, R> {
        Builder<A, R> required(R request);

        Builder<A, R> required(Collection<R> requests);

        Builder<A, R> optional(R request);

        Builder<A, R> optional(Collection<R> requests);

        DoubleContainer<A, R> build();
    }
}
