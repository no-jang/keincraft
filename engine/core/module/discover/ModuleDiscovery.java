package engine.core.module.discover;

import java.util.List;

public interface ModuleDiscovery {
    void addSource(ModuleSource source);

    List<ModuleDefinition> discoverModules();
}
