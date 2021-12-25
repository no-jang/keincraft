package engine.graphics.vulkan.device;

import engine.collections.container.Container;
import engine.graphics.vulkan.device.properties.DeviceExtension;
import engine.graphics.vulkan.device.properties.DeviceFeature;
import engine.graphics.vulkan.queue.properties.QueueContainer;
import engine.graphics.vulkan.util.function.VkFunction;
import engine.util.pointer.DestroyableReferencePointer;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkDevice;

public class Device extends DestroyableReferencePointer<VkDevice> {
    private final PhysicalDevice physicalDevice;
    private final QueueContainer queues;
    @Nullable
    private final Container<DeviceExtension> extensions;
    @Nullable
    private final Container<DeviceFeature> features;

    public Device(VkDevice reference,
                  PhysicalDevice physicalDevice,
                  QueueContainer queues,
                  @Nullable Container<DeviceExtension> extensions,
                  @Nullable Container<DeviceFeature> features) {
        super(reference);

        this.physicalDevice = physicalDevice;
        this.queues = queues;
        this.extensions = extensions;
        this.features = features;
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

    public QueueContainer getQueues() {
        return queues;
    }

    @Nullable
    public Container<DeviceExtension> getExtensions() {
        return extensions;
    }

    @Nullable
    public Container<DeviceFeature> getFeatures() {
        return features;
    }
}
