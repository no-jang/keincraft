package engine.graphics.vulkan.instance.properties;

import engine.graphics.vulkan.instance.extension.ExtensionContainer;
import engine.graphics.vulkan.instance.extension.properties.InstanceExtension;
import engine.graphics.vulkan.instance.extension.properties.InstanceLayer;
import org.checkerframework.checker.nullness.qual.Nullable;

public class InstanceProperties {
    @Nullable
    private final ExtensionContainer<InstanceExtension> enabledExtensions;
    @Nullable
    private final ExtensionContainer<InstanceLayer> enabledLayers;

    public InstanceProperties(@Nullable ExtensionContainer<InstanceExtension> enabledExtensions,
                              @Nullable ExtensionContainer<InstanceLayer> enabledLayers) {
        this.enabledExtensions = enabledExtensions;
        this.enabledLayers = enabledLayers;
    }

    @Nullable
    public ExtensionContainer<InstanceExtension> getEnabledExtensions() {
        return enabledExtensions;
    }

    @Nullable
    public ExtensionContainer<InstanceLayer> getEnabledLayers() {
        return enabledLayers;
    }
}
