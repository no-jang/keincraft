package engine.graphics.vulkan.device;

import engine.helper.pointer.DestroyableReferencePointer;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkDevice;

public class Device extends DestroyableReferencePointer<VkDevice> {
    public Device(VkDevice reference) {
        super(reference);
    }

    @Override
    protected void destroy(VkDevice reference) {
        VK10.vkDestroyDevice(reference, null);
    }
}
