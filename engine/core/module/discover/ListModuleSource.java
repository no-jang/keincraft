package engine.core.module.discover;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * {@link ModuleSource} simply containing a list of classes.
 */
public class ListModuleSource implements ModuleSource {
    private final List<Class<?>> types;

    public ListModuleSource() {
        types = new ArrayList<>();
    }

    /**
     * Adds a class to the list and validate that it's a suitable module class.
     *
     * @param type class to add and validate
     */
    public void addClass(Class<?> type) {
        ModuleClassValidator.validateModuleClass(type);
        types.add(type);
    }

    /**
     * Adds and validates a {@link Collection} of types
     *
     * @param types collection of types
     */
    public void addClasses(Collection<Class<?>> types) {
        for (Class<?> type : types) {
            addClass(type);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Class<?>> discoverModuleClasses() {
        return types;
    }
}
