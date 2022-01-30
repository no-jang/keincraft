package engine.collection.trait;

public interface SizeTrait {
    int size();

    default boolean isEmpty() {
        return size() <= 0;
    }

    default boolean isNotEmpty() {
        return size() > 0;
    }
}