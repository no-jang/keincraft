package client.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public abstract class Pool<T> {
    private final int max;
    private final Deque<T> freeObjects;

    private int peak;

    public Pool() {
        this(16, Integer.MAX_VALUE);
    }

    public Pool(int initialCapacity) {
        this(initialCapacity, Integer.MAX_VALUE);
    }

    public Pool(int initialCapacity, int max) {
        this.max = max;
        this.freeObjects = new ArrayDeque<>(initialCapacity);
    }

    abstract protected T newObject();

    public T get() {
        return freeObjects.size() == 0 ? newObject() : freeObjects.pop();
    }

    public void put(T object) {
        if (freeObjects.size() < max) {
            freeObjects.add(object);
            peak = Math.max(peak, freeObjects.size());
        }

        reset(object);
    }

    public void putAll(List<T> objects) {
        for (T object : objects) {
            if (freeObjects.size() < max) {
                freeObjects.add(object);
            }

            reset(object);
        }

        peak = Math.max(peak, freeObjects.size());
    }

    public void clear() {
        freeObjects.clear();
    }

    public int getFree() {
        return freeObjects.size();
    }

    protected void reset(T object) {
        if (object instanceof Poolable) {
            ((Poolable) object).reset();
        }
    }
}
