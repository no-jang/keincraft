package engine.collections;

import java.util.Collection;

public class DefaultImmutableCollection<E> extends AbstractImmutableCollection<Collection<E>, E> {
    public DefaultImmutableCollection(Collection<E> wrapped) {
        super(wrapped);
    }
}
