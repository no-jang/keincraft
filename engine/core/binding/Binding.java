package engine.core.binding;

import engine.core.provider.Provider;

public interface Binding<T> {
    Class<T> getType();

    Provider<T> getProvider();
}
