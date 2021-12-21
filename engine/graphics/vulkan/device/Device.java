package engine.graphics.vulkan.device;

import engine.graphics.vulkan.util.function.VkFunction;
import engine.util.pointer.DestroyableReferencePointer;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkDevice;

public class Device extends DestroyableReferencePointer<VkDevice> {
    private final PhysicalDevice physicalDevice;

    public Device(VkDevice reference, PhysicalDevice physicalDevice) {
        super(reference);

        this.physicalDevice = physicalDevice;
    }

    @Override
    protected void destroy(VkDevice reference) {
        VK10.vkDestroyDevice(reference, null);
    }

    public void waitIdle() {
        VkFunction.execute(() -> VK10.vkDeviceWaitIdle(reference));
    }

    public PhysicalDevice getPhysicalDevice() {
        return physicalDevice;
    }
}
