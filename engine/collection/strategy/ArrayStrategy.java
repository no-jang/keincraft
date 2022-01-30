package engine.collection.strategy;

public interface ArrayStrategy<T> {
    T[] newArray();

    T[] ensureCapacity(T[] array, int size, int capacityDifference);
}
