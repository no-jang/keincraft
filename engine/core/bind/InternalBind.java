package engine.core.bind;

import engine.core.provider.Provider;

public interface InternalBind<T> extends Bind<T> {
    Class<T> getType();

    Provider<T> getProvider();
}
