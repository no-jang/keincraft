package engine.collections.map;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Maps {
    public static <K1, K2, V1, V2> ImmutableMap<K2, V2> immutableMap(Map<K1, V1> map, Function<K1, K2> keyTransform, Function<V1, V2> valueTransform) {
        return new DefaultImmutableMap<>(map.entrySet().stream()
                .collect(Collectors.toMap(entry -> keyTransform.apply(entry.getKey()), entry -> valueTransform.apply(entry.getValue()))));
    }
}
