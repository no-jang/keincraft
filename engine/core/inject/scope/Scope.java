package engine.core.inject.scope;

import engine.core.inject.container.Container;

public interface Scope {
    <T> T get(Class<T> type);

    void install(Container container);
}
