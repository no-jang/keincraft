package engine.core.bind;

import engine.core.provider.InstanceProvider;
import engine.core.provider.Provider;
import org.checkerframework.checker.nullness.qual.Nullable;

public class DefaultInternalBind<T> implements InternalBind<T> {
    private final Class<T> type;

    @Nullable
    private Provider<T> provider;

    public DefaultInternalBind(Class<T> type) {
        this.type = type;
    }

    @Override
    public Bind<T> fromInstance(T instance) {
        this.provider = new InstanceProvider<>(instance);
        return this;
    }

    @Override
    public Bind<T> fromProvider(Provider<T> provider) {
        this.provider = provider;
        return this;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public Provider<T> getProvider() {
        if (provider == null) {
            throw new IllegalStateException("There is not instance provider defined for this bind.");
        }

        return provider;
    }
}
