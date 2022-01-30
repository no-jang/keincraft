package engine.collection.identificator;

public interface Identificator<T> {
    boolean equals(T object1, T object2);

    int hashCode(T object);
}
