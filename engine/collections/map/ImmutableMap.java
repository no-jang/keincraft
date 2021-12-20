package engine.collections.map;

import engine.collections.ImmutableCollection;
import engine.collections.set.ImmutableSet;

import java.util.Map;

public interface ImmutableMap<K, V> {
    int size();

    boolean isEmpty();

    boolean containsKey(K key);

    boolean containsValue(V value);

    V get(K key);

    ImmutableSet<K> keySet();

    ImmutableCollection<V> values();

    Map<K, V> toMutable();
}
