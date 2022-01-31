package engine.collection.list;

import engine.collection.stack.Stack;
import engine.collection.trait.contain.ContainIndexTrait;
import engine.collection.trait.order.TailTrait;

public interface List<T> extends Stack<T>, TailTrait<T>, ContainIndexTrait<T> {

}
