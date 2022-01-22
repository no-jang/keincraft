package engine.core.inject.bind;

public interface Bind<T> {
    Class<T> getType();

    interface CanBeBound {

    }

    interface CanBeSingleton {

    }
}
