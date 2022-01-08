package engine.core.container;

import engine.core.bind.Bind;
import engine.core.bind.DefaultInternalBind;
import engine.core.bind.InternalBind;
import engine.core.binding.Binding;
import engine.core.binding.DefaultBinding;

import java.util.ArrayList;
import java.util.List;

public class DefaultContainer implements Container {
    private final List<InternalBind<?>> pendingBinds;
   // private final Multimap<Class<?>, Binding<?>> bindings;

    public DefaultContainer() {
        pendingBinds = new ArrayList<>();
        //bindings = ArrayListMultimap.create();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> type) {
        updateBindings();

/*        Collection<Binding<?>> bindings = this.bindings.get(type);
        if (bindings == null) {
            throw new IllegalArgumentException("Type " + type + " wasn't bound");
        }

        if(bindings.size() > 1) {
            throw new IllegalArgumentException("Type " + type + " is more than one time bound. It must be used by resolveAll");
        }

        Provider<T> provider = binding.getProvider();*/
        //return provider.getInstance();
        return null;
    }

    @Override
    public <T> Bind<T> bind(Class<T> type) {
        InternalBind<T> bind = new DefaultInternalBind<>(type);
        pendingBinds.add(bind);
        return bind;
    }

    private void updateBindings() {
        if (pendingBinds.isEmpty()) {
            return;
        }

        pendingBinds.removeIf(this::updateBindings);
    }

    private <T> boolean updateBindings(InternalBind<T> bind) {
        Class<T> type = bind.getType();
        Binding<T> binding = new DefaultBinding<>(type, bind.getProvider());

        //bindings.put(type, binding);
        return true;
    }
}
