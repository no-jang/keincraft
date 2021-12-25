package engine.graphics.vulkan.instance;

import engine.collections.container.Container;
import engine.graphics.vulkan.instance.properties.InstanceExtension;
import engine.graphics.vulkan.instance.properties.InstanceLayer;
import engine.util.pointer.DestroyableReferencePointer;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.lwjgl.vulkan.EXTDebugReport;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkInstance;

public class Instance extends DestroyableReferencePointer<VkInstance> {
    private final long messageCallbackHandle;
    @Nullable
    private final Container<InstanceExtension> extensions;
    @Nullable
    private final Container<InstanceLayer> layers;

    public Instance(VkInstance reference,
                    long messageCallbackHandle,
                    @Nullable Container<InstanceExtension> extensions,
                    @Nullable Container<InstanceLayer> layers) {
        super(reference);
        this.messageCallbackHandle = messageCallbackHandle;
        this.extensions = extensions;
        this.layers = layers;
    }

    @Override
    protected void destroy(VkInstance reference) {
        if (messageCallbackHandle != -1) {
            EXTDebugReport.vkDestroyDebugReportCallbackEXT(reference, messageCallbackHandle, null);
        }

        VK10.vkDestroyInstance(reference, null);
    }

    public long getMessageCallbackHandle() {
        return messageCallbackHandle;
    }

    public Container<InstanceExtension> getExtensions() {
        return extensions;
    }

    public Container<InstanceLayer> getLayers() {
        return layers;
    }
}
