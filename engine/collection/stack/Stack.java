package engine.collection.stack;

import engine.collection.index.IndexedCollection;

/**
 * A last in first out (LIFO) collection. The last element that is added is the fist one that is removed like a pile or
 * stack of books.
 *
 * @param <T> type of the containing elements
 */
public interface Stack<T> extends IndexedCollection<T> {

}
