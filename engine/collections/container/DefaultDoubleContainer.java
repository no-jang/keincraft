package engine.collections.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DefaultDoubleContainer<A, R> implements DoubleContainer<A, R> {
    private final List<A> available;
    private final List<R> requested;

    public DefaultDoubleContainer(List<A> available, List<R> requested) {
        this.available = available;
        this.requested = requested;
    }

    @Override
    public List<A> getAvailable() {
        return available;
    }

    @Override
    public List<R> getRequested() {
        return requested;
    }

    public abstract static class AbstractBuilder<A, R> implements DoubleContainer.Builder<A, R> {
        protected final List<A> available;
        protected final List<R> requested;

        public AbstractBuilder(List<A> available) {
            this.available = available;
            this.requested = new ArrayList<>();
        }

        protected abstract boolean isAvailable(R request);

        @Override
        public Builder<A, R> required(R request) {
            if (!isAvailable(request)) {
                throw new IllegalArgumentException("Request " + request + " is not available");
            }

            requested.add(request);
            return this;
        }

        @Override
        public Builder<A, R> required(Collection<R> requests) {
            for (R request : requests) {
                required(request);
            }

            return this;
        }

        @Override
        public Builder<A, R> optional(R request) {
            if (!isAvailable(request)) {
                return this;
            }

            requested.add(request);
            return this;
        }

        @Override
        public Builder<A, R> optional(Collection<R> requests) {
            for (R request : requests) {
                optional(request);
            }

            return this;
        }
    }
}
