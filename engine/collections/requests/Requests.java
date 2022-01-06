package engine.collections.requests;

import java.util.Collection;
import java.util.List;

// TODO Rename in Request
public interface Requests<R> {
    List<R> getAvailable();

    List<R> getRequested();

    interface Builder<R> {
        Builder<R> required(R request);

        Builder<R> required(Collection<R> requests);

        Builder<R> optional(R request);

        Builder<R> optional(Collection<R> requests);

        Requests<R> build();
    }
}
