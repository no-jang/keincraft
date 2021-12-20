package engine.collections.container;

import engine.collections.list.ImmutableList;

import java.util.Collection;

public interface Container<R> {
    ImmutableList<R> getAvailable();

    ImmutableList<R> getRequested();

    interface Builder<R> {
        Builder<R> required(R request);

        Builder<R> required(Collection<R> requests);

        Builder<R> optional(R request);

        Builder<R> optional(Collection<R> requests);

        Container<R> build();
    }
}
