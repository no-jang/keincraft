package client.graphics2.vk.device;

import client.graphics2.vk.util.Check;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.IntBuffer;
import java.util.stream.IntStream;

/**
 * Represent a vulkan graphical device that can be used for drawing
 */
public class Device {
    private final VkDevice handle;
    private final VkQueue graphicsQueue;
    private final VkQueue presentQueue;

    private int graphicsQueueFamilyIndex = -1;
    private int presentQueueFamilyIndex = -1;

    /**
     * Creates new device from its physical device and the surface it should present to.
     * Initializes a graphics and present queue as well as features and extensions.
     *
     * @param stack          memory stack
     * @param physicalDevice corresponding physical device
     * @param surface        surface where it should present to
     */
    public Device(MemoryStack stack, PhysicalDevice physicalDevice, Surface surface) {
        // Get available queue families
        IntBuffer pQueueFamilyCount = stack.mallocInt(1);
        VK10.vkGetPhysicalDeviceQueueFamilyProperties(physicalDevice.getHandle(), pQueueFamilyCount, null);
        int queueFamilyCount = pQueueFamilyCount.get(0);

        // Get available queue families
        VkQueueFamilyProperties.Buffer pQueueFamilies = VkQueueFamilyProperties.malloc(queueFamilyCount, stack);
        VK10.vkGetPhysicalDeviceQueueFamilyProperties(physicalDevice.getHandle(), pQueueFamilyCount, pQueueFamilies);

        // Enumerate all queues and find graphics and presentation queue
        for (int i = 0; i < queueFamilyCount; i++) {
            VkQueueFamilyProperties queueFamily = pQueueFamilies.get(i);

            // Check if graphics queue
            if ((queueFamily.queueFlags() & VK10.VK_QUEUE_GRAPHICS_BIT) != 0) {
                this.graphicsQueueFamilyIndex = i;
            }

            // Check if present queue
            IntBuffer pPresentSupported = stack.mallocInt(1);
            Check.vkCheck(KHRSurface.vkGetPhysicalDeviceSurfaceSupportKHR(physicalDevice.getHandle(), i, surface.getHandle(), pPresentSupported),
                    "Failed to get queue family present support");
            if (pPresentSupported.get(0) == VK10.VK_TRUE) {
                presentQueueFamilyIndex = i;
            }

            // Check if all queue families are found and skip the rest
            if (graphicsQueueFamilyIndex != -1 && presentQueueFamilyIndex != -1) {
                break;
            }
        }

        // Check if all queue families are found, if not error
        if (graphicsQueueFamilyIndex == -1 && presentQueueFamilyIndex == -1) {
            throw new RuntimeException("Failed to find present and graphics queue families for device");
        }

        // Create queue create info for every unique queue family
        int[] uniqueQueueFamiliesIndices = IntStream.of(graphicsQueueFamilyIndex, presentQueueFamilyIndex).distinct().toArray();
        VkDeviceQueueCreateInfo.Buffer pQueueCreateInfos = VkDeviceQueueCreateInfo.malloc(uniqueQueueFamiliesIndices.length, stack);
        for (int i = 0; i < uniqueQueueFamiliesIndices.length; i++) {
            pQueueCreateInfos.get(i)
                    .sType$Default()
                    .flags(0)
                    .pNext(0)
                    .queueFamilyIndex(uniqueQueueFamiliesIndices[i])
                    .pQueuePriorities(stack.floats(1.0f));
        }

        // Create device
        VkDeviceCreateInfo createInfo = VkDeviceCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .pQueueCreateInfos(pQueueCreateInfos)
                .pEnabledFeatures(physicalDevice.getFeatures())
                .ppEnabledExtensionNames(physicalDevice.getExtensions())
                .ppEnabledLayerNames(null);

        PointerBuffer pDevice = stack.mallocPointer(1);
        Check.vkCheck(VK10.vkCreateDevice(physicalDevice.getHandle(), createInfo, null, pDevice), "Failed to create logical device");
        handle = new VkDevice(pDevice.get(0), physicalDevice.getHandle(), createInfo);

        // Get device queues
        graphicsQueue = createQueue(stack, graphicsQueueFamilyIndex);
        presentQueue = createQueue(stack, presentQueueFamilyIndex);
    }

    private VkQueue createQueue(MemoryStack stack, int familyIndex) {
        PointerBuffer pQueue = stack.mallocPointer(1);
        VK10.vkGetDeviceQueue(handle, familyIndex, 0, pQueue);
        return new VkQueue(pQueue.get(0), handle);
    }

    /**
     * Destroy the device
     */
    public void destroy() {
        VK10.vkDestroyDevice(handle, null);
    }

    /**
     * Wait until the device has processed all queue entries
     */
    public void waitIdle() {
        VK10.vkDeviceWaitIdle(handle);
    }

    /**
     * @return internal vulkan device handle
     */
    public VkDevice getHandle() {
        return handle;
    }

    /**
     * @return queue where all render tasks are submitted to
     */
    public VkQueue getGraphicsQueue() {
        return graphicsQueue;
    }

    /**
     * @return queue where all presentation tasks are submitted to
     */
    public VkQueue getPresentQueue() {
        return presentQueue;
    }

    /**
     * @return family index for graphics queue
     */
    public int getGraphicsQueueFamilyIndex() {
        return graphicsQueueFamilyIndex;
    }

    /**
     * @return family index for graphics queue
     */
    public int getPresentQueueFamilyIndex() {
        return presentQueueFamilyIndex;
    }
}
