package client.graphics.vk.device;

import client.graphics.vk.device.properties.DeviceExtension;
import client.graphics.vk.device.properties.DeviceFeature;
import client.graphics.vk.device.properties.DeviceInfo;
import client.graphics.vk.memory.MemoryContext;
import client.graphics.vk.models.function.CheckFunction;
import client.graphics.vk.models.pointers.DestroyableReferencePointer;
import client.graphics.vk.queue.QueueFamily;
import common.util.Collections;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkDeviceCreateInfo;
import org.lwjgl.vulkan.VkDeviceQueueCreateInfo;
import org.lwjgl.vulkan.VkPhysicalDeviceFeatures;
import org.tinylog.Logger;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LogicalDevice extends DestroyableReferencePointer<VkDevice> {
    private final VkDevice handle;
    private final PhysicalDevice physicalDevice;
    private final DeviceInfo info;

    public LogicalDevice(PhysicalDevice physicalDevice, DeviceInfo info) {
        this.physicalDevice = physicalDevice;
        this.info = info;

        MemoryStack stack = MemoryContext.getStack();

        // Test if all requested queue families are found on the physical device
        for (QueueFamily family : info.getQueuePriorities().keySet()) {
            if (!physicalDevice.getQueueFamilies().contains(family)) {
                throw new IllegalArgumentException(physicalDevice + " does not have a family queue " + family);
            }
        }

        List<DeviceExtension> requiredExtensions = new ArrayList<>(info.getRequiredExtensions());
        List<DeviceExtension> optionalExtensions = new ArrayList<>(info.getOptionalExtensions());
        List<DeviceExtension> availableExtensions = physicalDevice.getExtensions();
        List<DeviceExtension> enabledExtensions = info.getEnabledExtensions();

        // Check every extension if it should be added
        Collections.checkRequiredOptional(availableExtensions, requiredExtensions, optionalExtensions, enabledExtensions);

        // If a required extension is not available throw exception
        if (!requiredExtensions.isEmpty()) {
            throw new IllegalArgumentException(physicalDevice + "does not have required extensions: " +
                    requiredExtensions.stream()
                            .map(Enum::name)
                            .collect(Collectors.joining(", ")));
        }

        // If a optional extension is not available log warning
        if (!optionalExtensions.isEmpty()) {
            Logger.debug("{} does not have optional extensions: {}", physicalDevice, optionalExtensions);
        }

        PointerBuffer pExtensions = DeviceExtension.toVulkanBuffer(enabledExtensions);

        List<DeviceFeature> requiredFeatures = new ArrayList<>(info.getRequiredFeatures());
        List<DeviceFeature> optionalFeatures = new ArrayList<>(info.getOptionalFeatures());
        List<DeviceFeature> availableFeatures = physicalDevice.getFeatures();
        List<DeviceFeature> enabledFeatures = info.getEnabledFeatures();

        // Check every feature if it should be added
        Collections.checkRequiredOptional(availableFeatures, requiredFeatures, optionalFeatures, enabledFeatures);

        // If required features is not available throw exception
        if (!requiredFeatures.isEmpty()) {
            throw new IllegalArgumentException(physicalDevice + " does not have required features: " +
                    requiredFeatures.stream()
                            .map(Enum::name)
                            .collect(Collectors.joining(", ")));
        }

        // If optional feature is not available log warning
        if (!optionalFeatures.isEmpty()) {
            Logger.debug("{} does not have optional features: {}", physicalDevice, optionalFeatures);
        }

        VkPhysicalDeviceFeatures features = DeviceFeature.toVulkanFeatures(enabledFeatures);

        VkDeviceQueueCreateInfo.Buffer pQueues = VkDeviceQueueCreateInfo.malloc(info.getQueuePriorities().size(), stack);

        VkDeviceCreateInfo createInfo = VkDeviceCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .pQueueCreateInfos(pQueues)
                .ppEnabledExtensionNames(pExtensions)
                .pEnabledFeatures(features);

        // Create one queue entry for every requested queue family
        for (Map.Entry<QueueFamily, List<Float>> queueEntry : info.getQueuePriorities().entrySet()) {
            QueueFamily family = queueEntry.getKey();
            List<Float> priorities = queueEntry.getValue();

            FloatBuffer pPriorities = stack.mallocFloat(priorities.size());
            for (int j = 0; j < priorities.size(); j++) {
                pPriorities.put(j, priorities.get(j));
            }

            VkDeviceQueueCreateInfo queue = VkDeviceQueueCreateInfo.malloc(stack)
                    .sType$Default()
                    .flags(0)
                    .pNext(0)
                    .queueFamilyIndex(family.getIndex())
                    .pQueuePriorities(pPriorities);

            pQueues.put(queue);
        }

        PointerBuffer pHandle = stack.mallocPointer(1);
        CheckFunction.execute(() -> VK10.vkCreateDevice(physicalDevice.getReference(), createInfo, null, pHandle));
        handle = new VkDevice(pHandle.get(0), physicalDevice.getReference(), createInfo);
    }

    public void printDevice() {
        Logger.debug(this::toPropertyString);
    }

    @Override
    protected void internalDestroy() {
        VK10.vkDestroyDevice(handle, null);
    }

    @Override
    protected long getInternalHandle() {
        return handle.address();
    }

    @Override
    protected VkDevice getInternalReference() {
        return handle;
    }

    public PhysicalDevice getPhysicalDevice() {
        return physicalDevice;
    }

    public DeviceInfo getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "LogicalDevice[physicalDevice=" + physicalDevice + ']';
    }

    public String toPropertyString() {
        return "LogicalDevice[" +
                "\n physicalDevice=" + physicalDevice +
                ",\n info=" + info +
                ']';
    }
}
