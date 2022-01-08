package engine.core.container;

import engine.core.bind.Bind;

public interface Container {
    <T> T resolve(Class<T> type);

    <T> Bind<T> bind(Class<T> type);
}
