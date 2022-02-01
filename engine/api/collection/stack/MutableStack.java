package engine.api.collection.stack;

import engine.api.collection.sequence.MutableSequence;
import engine.api.collection.trait.mutable.StackTrait;

public interface MutableStack<T> extends Stack<T>, MutableSequence<T>, StackTrait<T> {

}
