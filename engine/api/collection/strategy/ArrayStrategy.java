package engine.api.collection.strategy;

public interface ArrayStrategy<T> {
    T[] newArray();

    T[] growArray(T[] array, int size, int increaseCapacity);

    T[] shrinkArray(T[] array, int size, int reduceCapacity);
}
