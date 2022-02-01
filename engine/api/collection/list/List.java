package engine.api.collection.list;

import engine.api.collection.stack.Stack;
import engine.api.collection.trait.contain.IndexContainTrait;
import engine.api.collection.trait.index.LastIndexTrait;
import engine.api.collection.trait.order.TailTrait;

public interface List<T> extends Stack<T>, TailTrait<T>, IndexContainTrait<T>, LastIndexTrait<T> {

}
