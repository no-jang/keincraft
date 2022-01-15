package engine.collection;

import engine.collection.common.HasTail;

import java.util.Iterator;

public interface OrderedCollection<T> extends Collection<T>, HasTail<T> {
    Iterator<T> reverseIterator();
}
