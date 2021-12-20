package engine.collections.container;

import engine.collections.list.DefaultImmutableList;
import engine.collections.list.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DefaultContainer<R> implements Container<R> {
    private final ImmutableList<R> available;
    private final ImmutableList<R> requested;

    public DefaultContainer(ImmutableList<R> available, ImmutableList<R> requested) {
        this.available = available;
        this.requested = requested;
    }

    @Override
    public ImmutableList<R> getAvailable() {
        return available;
    }

    @Override
    public ImmutableList<R> getRequested() {
        return requested;
    }

    public static class Builder<R> implements Container.Builder<R> {
        private final ImmutableList<R> available;
        private final List<R> requested;

        public Builder(List<R> available) {
            this.available = new DefaultImmutableList<>(available);
            this.requested = new ArrayList<>();
        }

        @Override
        public Container.Builder<R> required(R request) {
            if (!available.contains(request)) {
                throw new IllegalArgumentException("Request " + request + " is not available");
            }

            requested.add(request);
            return this;
        }

        @Override
        public Container.Builder<R> required(Collection<R> requests) {
            for (R request : requests) {
                required(request);
            }

            return this;
        }

        @Override
        public Container.Builder<R> optional(R request) {
            if (!available.contains(request)) {
                return this;
            }

            requested.add(request);
            return this;
        }

        @Override
        public Container.Builder<R> optional(Collection<R> requests) {
            for (R request : requests) {
                optional(request);
            }

            return this;
        }

        @Override
        public Container<R> build() {
            return new DefaultContainer<>(available, new DefaultImmutableList<>(requested));
        }
    }
}
