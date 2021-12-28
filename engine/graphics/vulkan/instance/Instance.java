package engine.graphics.vulkan.instance;

import engine.collections.container.Container;
import engine.graphics.vulkan.instance.properties.InstanceExtension;
import engine.graphics.vulkan.instance.properties.InstanceLayer;
import engine.memory.holder.DestroyHolderBase;
import engine.memory.resource.Resource;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.lwjgl.vulkan.EXTDebugReport;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkInstance;

public class Instance extends DestroyHolderBase<Resource<Instance>> {
    private final VkInstance handle;
    private final long messageCallbackHandle;
    @Nullable
    private final Container<InstanceExtension> extensions;
    @Nullable
    private final Container<InstanceLayer> layers;

    public Instance(VkInstance handle,
                    long messageCallbackHandle,
                    @Nullable Container<InstanceExtension> extensions,
                    @Nullable Container<InstanceLayer> layers) {
        this.handle = handle;
        this.messageCallbackHandle = messageCallbackHandle;
        this.extensions = extensions;
        this.layers = layers;
    }

    @Override
    public void destroy() {
        super.destroy();

        if (messageCallbackHandle != -1) {
            EXTDebugReport.vkDestroyDebugReportCallbackEXT(handle, messageCallbackHandle, null);
        }

        VK10.vkDestroyInstance(handle, null);
    }

    public VkInstance getHandle() {
        return handle;
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
