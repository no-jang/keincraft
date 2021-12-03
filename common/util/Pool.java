package common.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Pool of objects which are dynamically created if needed
 *
 * This can be used to reduce object allocations and so garbage collections
 *
 * @param <T> type of objects
 */
public abstract class Pool<T> {
    private final int max;
    private final Deque<T> freeObjects;

    private int peak;

    /**
     * Creates a new pool with an initial capacity of 16 and unlimited entries
     */
    public Pool() {
        this(16, Integer.MAX_VALUE);
    }

    /**
     * Creates a new pool with specified capacity and unlimited entries
     *
     * Objects are not allocated until needed.
     *
     * @param initialCapacity initial capacity for Deque
     */
    public Pool(int initialCapacity) {
        this(initialCapacity, Integer.MAX_VALUE);
    }

    /**
     * Creates a new pool with specified capacity and specified max entries
     *
     * @param initialCapacity initial capacity for Deque
     * @param max maximal entries allowed
     */
    public Pool(int initialCapacity, int max) {
        this.max = max;
        this.freeObjects = new ArrayDeque<>(initialCapacity);
    }

    /**
     * Creates a new object
     * @return new object
     */
    abstract protected T newObject();

    /**
     * Gets an unused object or creates a new one
     *
     * @return object
     */
    public T get() {
        return freeObjects.size() == 0 ? newObject() : freeObjects.pop();
    }

    /**
     * Puts an object in pool.
     *
     * This declares the object as "no longer in use". It will only be added if the count of free objects is under the
     * maximum. If the object implements Poolable the reset function will be called to reset all variables.
     *
     * @param object object "no longer in use"
     */
    public void put(T object) {
        if (freeObjects.size() < max) {
            freeObjects.add(object);
            peak = Math.max(peak, freeObjects.size());
        }

        reset(object);
    }

    /**
     * Puts a list of objects
     *
     * This declares the objects as "no longer in use". It will only be added if the count of free objects is under the
     * maximum. If the objects implements Poolable the reset function will be called to reset all variables.
     *
     * @param objects objects "no longer in use"
     */
    public void putAll(List<T> objects) {
        for (T object : objects) {
            if (freeObjects.size() < max) {
                freeObjects.add(object);
            }

            reset(object);
        }

        peak = Math.max(peak, freeObjects.size());
    }

    /**
     * Clears all free objects
     */
    public void clear() {
        freeObjects.clear();
    }

    /**
     * Get free object count
     * @return free object count
     */
    public int getFree() {
        return freeObjects.size();
    }

    /**
     * Resets an object to it's default values
     *
     * @param object object to be reset
     */
    protected void reset(T object) {
        if (object instanceof Poolable) {
            ((Poolable) object).reset();
        }
    }
}
