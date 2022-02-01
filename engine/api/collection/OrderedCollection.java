package engine.api.collection;

import engine.api.collection.trait.order.HeadTrait;

public interface OrderedCollection<T> extends Collection<T>, HeadTrait<T> {

}
