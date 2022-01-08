package engine.requests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DefaultRequests<R> implements Requests<R> {
    private final List<R> available;
    private final List<R> requested;

    public DefaultRequests(List<R> available, List<R> requested) {
        this.available = available;
        this.requested = requested;
    }

    @Override
    public List<R> getAvailable() {
        return available;
    }

    @Override
    public List<R> getRequested() {
        return requested;
    }

    public static class Builder<R> implements Requests.Builder<R> {
        private final List<R> available;
        private final List<R> requested;

        public Builder(List<R> available) {
            this.available = available;
            this.requested = new ArrayList<>();
        }

        @Override
        public Requests.Builder<R> required(R request) {
            if (!available.contains(request)) {
                throw new IllegalArgumentException("Request " + request + " is not available");
            }

            requested.add(request);
            return this;
        }

        @Override
        public Requests.Builder<R> required(Collection<R> requests) {
            for (R request : requests) {
                required(request);
            }

            return this;
        }

        @Override
        public Requests.Builder<R> optional(R request) {
            if (!available.contains(request)) {
                return this;
            }

            requested.add(request);
            return this;
        }

        @Override
        public Requests.Builder<R> optional(Collection<R> requests) {
            for (R request : requests) {
                optional(request);
            }

            return this;
        }

        @Override
        public Requests<R> build() {
            return new DefaultRequests<>(available, requested);
        }
    }
}
