package engine.collections.list;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Lists {
    public static <E1, E2> ImmutableList<E2> immutableList(List<E1> list, Function<E1, E2> transform) {
        return new DefaultImmutableList<>(list.stream()
                .map(transform)
                .collect(Collectors.toList()));
    }
}
