package client.graphics.vk.device;

import client.graphics.vk.device.properties.DeviceExtension;
import client.graphics.vk.device.properties.DeviceFeature;
import client.graphics.vk.models.pointers.DestroyableReferencePointer;
import client.graphics.vk.queue.QueueFamily;
import org.lwjgl.vulkan.VkDevice;

import java.util.List;
import java.util.Map;

public class LogicalDevice extends DestroyableReferencePointer<VkDevice> {
    private final VkDevice device;

    public LogicalDevice(PhysicalDevice physicalDevice, Map<QueueFamily, List<Float>> queueFamilyPriorities,
                         List<DeviceExtension> extensions, List<DeviceFeature> features) {

    }

    @Override
    protected void internalDestroy() {

    }

    @Override
    protected long getInternalHandle() {
        return device.address();
    }

    @Override
    protected VkDevice getInternalReference() {
        return device;
    }
}
