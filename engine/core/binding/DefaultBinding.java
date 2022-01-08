package engine.core.binding;

import engine.core.provider.Provider;

public class DefaultBinding<T> implements Binding<T> {
    private final Class<T> type;
    private final Provider<T> provider;

    public DefaultBinding(Class<T> type, Provider<T> provider) {
        this.type = type;
        this.provider = provider;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public Provider<T> getProvider() {
        return provider;
    }
}
