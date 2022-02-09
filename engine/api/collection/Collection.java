package engine.api.collection;

import engine.api.collection.trait.common.SizeTrait;
import engine.api.collection.trait.iterator.IteratorTrait;

public interface Collection<T> extends SizeTrait, IteratorTrait<T> {

}
