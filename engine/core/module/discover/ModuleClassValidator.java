package engine.core.module.discover;

import engine.core.module.Module;

/**
 * Class for checking if a class is suitable as module. Mainly used by {@link ModuleSource}s for discovering classes
 * with modules.
 */
public final class ModuleClassValidator {

    /**
     * Validates that a class is suitable as a module. If not an IllegalArgumentException is thrown.
     * <pre>
     * A valid module class:
     * - is annotated with {@link Module} with name parameter
     * </pre>
     *
     * @param type class expected as valid module class
     * @see Module
     */
    public static void validateModuleClass(Class<?> type) {
        if (!type.isAnnotationPresent(Module.class)) {
            throw new IllegalArgumentException("Class " + type + " is not annotated with @Module and so can't be used as module");
        }
    }
}
