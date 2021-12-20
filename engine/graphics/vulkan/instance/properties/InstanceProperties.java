package engine.graphics.vulkan.instance.properties;

import engine.collections.container.Container;
import org.checkerframework.checker.nullness.qual.Nullable;

public class InstanceProperties {
    @Nullable
    private final Container<InstanceExtension> enabledExtensions;
    @Nullable
    private final Container<InstanceLayer> enabledLayers;

    public InstanceProperties(@Nullable Container<InstanceExtension> enabledExtensions,
                              @Nullable Container<InstanceLayer> enabledLayers) {
        this.enabledExtensions = enabledExtensions;
        this.enabledLayers = enabledLayers;
    }

    @Nullable
    public Container<InstanceExtension> getEnabledExtensions() {
        return enabledExtensions;
    }

    @Nullable
    public Container<InstanceLayer> getEnabledLayers() {
        return enabledLayers;
    }
}
