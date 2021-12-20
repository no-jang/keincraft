package engine.collections.set;

import engine.collections.ImmutableCollection;

import java.util.Set;

public interface ImmutableSet<E> extends ImmutableCollection<E> {
    @Override
    Set<E> toMutable();
}
