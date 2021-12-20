package engine.collections.set;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Sets {
    public static <E1, E2> ImmutableSet<E2> immutableSet(Set<E1> list, Function<E1, E2> transform) {
        return new DefaultImmutableSet<>(list.stream()
                .map(transform)
                .collect(Collectors.toSet()));
    }
}
