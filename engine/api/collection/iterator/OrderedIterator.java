package engine.api.collection.iterator;

import engine.api.collection.trait.iterator.PreviousTrait;

public interface OrderedIterator<T> extends Iterator<T>, PreviousTrait<T> {

}
