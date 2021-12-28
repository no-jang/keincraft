package engine.graphics.vulkan.device;

import engine.collections.container.Container;
import engine.graphics.vulkan.device.properties.DeviceExtension;
import engine.graphics.vulkan.device.properties.DeviceFeature;
import engine.graphics.vulkan.instance.Instance;
import engine.graphics.vulkan.queue.properties.QueueContainer;
import engine.graphics.vulkan.util.function.VkFunction;
import engine.memory.resource.Resource;
import engine.memory.resourceholder.DestroyResourceHolderBase;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkDevice;

public class Device extends DestroyResourceHolderBase<Device, Instance, Resource<Device>> {
    private final VkDevice handle;
    private final PhysicalDevice physicalDevice;
    private final QueueContainer queues;
    @Nullable
    private final Container<DeviceExtension> extensions;
    @Nullable
    private final Container<DeviceFeature> features;

    public Device(Instance owner,
                  VkDevice handle,
                  PhysicalDevice physicalDevice,
                  QueueContainer queues,
                  @Nullable Container<DeviceExtension> extensions,
                  @Nullable Container<DeviceFeature> features) {
        super(owner);
        this.handle = handle;
        this.physicalDevice = physicalDevice;
        this.queues = queues;
        this.extensions = extensions;
        this.features = features;
    }

    @Override
    public void destroy() {
        super.destroy();
        VK10.vkDestroyDevice(handle, null);
    }

    public void waitIdle() {
        VkFunction.execute(() -> VK10.vkDeviceWaitIdle(handle));
    }

    public VkDevice getHandle() {
        return handle;
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
