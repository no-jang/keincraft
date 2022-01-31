package engine.collection.list;

import engine.collection.stack.Stack;
import engine.collection.trait.contain.IndexContainTrait;
import engine.collection.trait.index.LastIndexTrait;
import engine.collection.trait.order.TailTrait;

public interface List<T> extends Stack<T>, TailTrait<T>, IndexContainTrait<T>, LastIndexTrait<T> {

}
