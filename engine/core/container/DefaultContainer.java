package engine.core.container;

import engine.core.bind.Bind;
import engine.core.bind.DefaultInternalBind;
import engine.core.bind.InternalBind;
import engine.core.binding.Binding;
import engine.core.binding.DefaultBinding;
import engine.core.provider.Provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultContainer implements Container {
    private final List<InternalBind<?>> pendingBinds;
    private final Map<Class<?>, Binding<?>> bindings;

    public DefaultContainer() {
        pendingBinds = new ArrayList<>();
        bindings = new HashMap<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> type) {
        Binding<T> binding = (Binding<T>) bindings.get(type);
        if (binding == null) {
            throw new IllegalArgumentException("Type " + type + " wasn't bound");
        }

        Provider<T> provider = binding.getProvider();
        return provider.getInstance();
    }

    @Override
    public <T> Bind<T> bind(Class<T> type) {
        InternalBind<T> bind = new DefaultInternalBind<>(type);
        pendingBinds.add(bind);
        return bind;
    }

    @Override
    public void resolveBindings() {
        pendingBinds.removeIf(this::resolveBinding);
    }

    private <T> boolean resolveBinding(InternalBind<T> bind) {
        Class<T> type = bind.getType();
        Binding<T> binding = new DefaultBinding<>(type, bind.getProvider());

        bindings.put(type, binding);
        return true;
    }
}
