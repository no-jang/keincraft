package engine.collection.strategy;

import engine.collection.util.Arrays;

public class DefaultArrayStrategy<T> implements ArrayStrategy<T> {
    public static final int DEFAULT_ARRAY_CAPACITY = 10;
    public static final int DEFAULT_MIN_GROW_SIZE = 10;
    public static final int DEFAULT_SHRINK_STEP_SIZE = 10;
    public static final int DEFAULT_SHRINK_THRESHOLD = 10;

    private final int defaultArrayCapacity;
    private final int minGrowSize;
    private final int shrinkStep;
    private final int shrinkThreshold;

    public DefaultArrayStrategy() {
        this(DEFAULT_ARRAY_CAPACITY, DEFAULT_MIN_GROW_SIZE, DEFAULT_SHRINK_STEP_SIZE, DEFAULT_SHRINK_THRESHOLD);
    }

    public DefaultArrayStrategy(int defaultArrayCapacity, int minGrowSize, int shrinkStep, int shrinkThreshold) {
        this.defaultArrayCapacity = defaultArrayCapacity;
        this.minGrowSize = minGrowSize;
        this.shrinkStep = shrinkStep;
        this.shrinkThreshold = shrinkThreshold;
    }

    @Override
    public T[] newArray() {
        return Arrays.unsafeCastNewArray(defaultArrayCapacity);
    }

    @Override
    public T[] growArray(T[] array, int size, int increaseCapacity) {
        int length = array.length;
        if (length - size < increaseCapacity) {
            int newSize = Math.max(length + minGrowSize, increaseCapacity);
            array = java.util.Arrays.copyOf(array, newSize);
        }
        return array;
    }

    @Override
    public T[] shrinkArray(T[] array, int size, int reduceCapacity) {
        int length = array.length;
        int capacityToReduce = length - size;
        if (capacityToReduce >= shrinkThreshold) {
            int stepCount = capacityToReduce / shrinkStep;
            int newSize = length - shrinkStep * stepCount;
            array = java.util.Arrays.copyOf(array, newSize);
        }
        return array;
    }
}
