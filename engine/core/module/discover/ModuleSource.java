package engine.core.module.discover;

import java.util.List;

/**
 * Module sources find module classes. A ModuleSource can also validate the classes with {@link ModuleClassValidator} but musn't
 */
public interface ModuleSource {

    /**
     * Discovers module classes.
     *
     * @return list of discovered classes
     */
    List<Class<?>> discoverModuleClassCandidates();
}
