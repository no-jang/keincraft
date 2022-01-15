package engine.collection.identification;

public interface Identificator<T> {
    boolean isIdentifiable(Object object);

    boolean equals(T object1, T object2);

    int hashCode(T object);
}
