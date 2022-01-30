package engine.collection.strategy;

import engine.collection.util.Arrays;

public class DefaultArrayStrategy<T> implements ArrayStrategy<T> {
    public static final int DEFAULT_NEW_ARRAY_CAPACITY = 10;
    public static final int DEFAULT_GROW_SIZE = 10;
    public static final int DEFAULT_SHRINK_SIZE = 10;
    public static final int DEFAULT_SHRINK_THRESHOLD = 10;

    private final int newArrayCapacity;
    private final int growSize;
    private final int shrinkSize;
    private final int shrinkThreshold;

    public DefaultArrayStrategy() {
        this(DEFAULT_NEW_ARRAY_CAPACITY, DEFAULT_GROW_SIZE, DEFAULT_SHRINK_SIZE, DEFAULT_SHRINK_THRESHOLD);
    }

    public DefaultArrayStrategy(int newArrayCapacity, int growSize, int shrinkSize, int shrinkThreshold) {
        this.newArrayCapacity = newArrayCapacity;
        this.growSize = growSize;
        this.shrinkSize = shrinkSize;
        this.shrinkThreshold = shrinkThreshold;
    }

    @Override
    public T[] newArray() {
        return Arrays.unsafeCastNewArray(newArrayCapacity);
    }

    @Override
    public T[] ensureCapacity(T[] array, int size, int capacityDifference) {
        if (capacityDifference < 0) {
            if (array.length - size >= shrinkThreshold) {
                array = java.util.Arrays.copyOf(array, array.length - shrinkSize);
            }
        }

        if (capacityDifference > 0) {
            if (array.length - size < capacityDifference) {
                array = java.util.Arrays.copyOf(array, array.length + growSize);
            }
        }

        return array;
    }
}
