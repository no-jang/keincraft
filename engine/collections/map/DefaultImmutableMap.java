package engine.collections.map;

import engine.collections.DefaultImmutableCollection;
import engine.collections.ImmutableCollection;
import engine.collections.set.DefaultImmutableSet;
import engine.collections.set.ImmutableSet;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

public class DefaultImmutableMap<K, V> implements ImmutableMap<K, V> {
    private final Map<K, V> wrapped;

    @Nullable
    private ImmutableSet<K> keySet;
    @Nullable
    private ImmutableCollection<V> values;

    public DefaultImmutableMap(Map<K, V> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public int size() {
        return wrapped.size();
    }

    @Override
    public boolean isEmpty() {
        return wrapped.isEmpty();
    }

    @Override
    public boolean containsKey(K key) {
        return wrapped.containsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        return wrapped.containsValue(value);
    }

    @Override
    public V get(K key) {
        return wrapped.get(key);
    }

    @Override
    public ImmutableSet<K> keySet() {
        if (keySet == null) {
            keySet = new DefaultImmutableSet<>(wrapped.keySet());
        }

        return keySet;
    }

    @Override
    public ImmutableCollection<V> values() {
        if (values == null) {
            values = new DefaultImmutableCollection<>(wrapped.values());
        }

        return values;
    }

    @Override
    public Map<K, V> toMutable() {
        return wrapped;
    }

    @Override
    public String toString() {
        return wrapped.toString();
    }
}
