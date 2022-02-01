package engine.api.collection.sequence;

import engine.api.collection.OrderedCollection;
import engine.api.collection.trait.common.DuplicateTrait;

public interface Sequence<T> extends OrderedCollection<T>, DuplicateTrait {

}
