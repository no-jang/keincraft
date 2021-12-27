package engine.graphics.vulkan.instance;

import engine.collections.container.Container;
import engine.graphics.vulkan.instance.properties.InstanceExtension;
import engine.graphics.vulkan.instance.properties.InstanceLayer;
import engine.memory.ownable.Ownable;
import engine.memory.owner.DestroyableOwnerHandle;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.lwjgl.vulkan.EXTDebugReport;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkInstance;

public class Instance extends DestroyableOwnerHandle<Ownable<Instance>, VkInstance> {
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
    protected void doDestroy() {
        if (messageCallbackHandle != -1) {
            EXTDebugReport.vkDestroyDebugReportCallbackEXT(handle, messageCallbackHandle, null);
        }

        VK10.vkDestroyInstance(handle, null);
    }

    public long getMessageCallbackHandle() {
        return messageCallbackHandle;
    }

    @Nullable
    public Container<InstanceExtension> getExtensions() {
        return extensions;
    }

    @Nullable
    public Container<InstanceLayer> getLayers() {
        return layers;
    }
}
