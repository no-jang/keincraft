package engine.collection.list;

import engine.collection.stack.Stack;
import engine.collection.trait.ContainIndexTrait;
import engine.collection.trait.TailTrait;

public interface List<T> extends Stack<T>, TailTrait<T>, ContainIndexTrait<T> {

}
