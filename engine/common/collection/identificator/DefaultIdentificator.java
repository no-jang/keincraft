package engine.common.collection.identificator;

import engine.api.collection.identificator.Identificator;

public class DefaultIdentificator<T> implements Identificator<T> {
    @Override
    public boolean equals(T object1, T object2) {
        return object1.equals(object2);
    }

    @Override
    public int hashCode(T object) {
        return object.hashCode();
    }
}
