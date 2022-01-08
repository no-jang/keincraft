package engine.core.bind;

import engine.core.provider.Provider;

public interface Bind<T> {
    Bind<T> fromInstance(T instance);

    Bind<T> fromProvider(Provider<T> provider);
}
