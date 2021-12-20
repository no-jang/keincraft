package engine.collections.set;

import engine.collections.AbstractImmutableCollection;

import java.util.Set;

public class DefaultImmutableSet<E> extends AbstractImmutableCollection<Set<E>, E> implements ImmutableSet<E> {
    public DefaultImmutableSet(Set<E> wrapped) {
        super(wrapped);
    }
}
