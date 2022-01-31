package engine.collection;

import engine.collection.trait.order.HeadTrait;

public interface OrderedCollection<T> extends Collection<T>, HeadTrait<T> {

}
