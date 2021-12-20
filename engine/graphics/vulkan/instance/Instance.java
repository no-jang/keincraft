package engine.graphics.vulkan.instance;

import engine.graphics.vulkan.instance.properties.InstanceProperties;
import engine.helper.pointer.DestroyableReferencePointer;
import org.lwjgl.vulkan.EXTDebugReport;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkInstance;

public class Instance extends DestroyableReferencePointer<VkInstance> {
    private final long messageCallbackHandle;
    private final InstanceProperties properties;

    public Instance(VkInstance reference, InstanceProperties properties, long messageCallbackHandle) {
        super(reference);

        this.messageCallbackHandle = messageCallbackHandle;
        this.properties = properties;
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

    public InstanceProperties getProperties() {
        return properties;
    }
}
