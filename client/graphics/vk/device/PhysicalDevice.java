package client.graphics.vk.device;

import client.graphics.vk.util.Check;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;
import org.tinylog.Logger;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Physical graphics device that supports vulkan
 */
public class PhysicalDevice {
    private static final String[] requiredExtensions = new String[]{
            KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME
    };

    private final VkPhysicalDevice handle;
    private final VkPhysicalDeviceProperties properties;
    private final VkPhysicalDeviceMemoryProperties memoryProperties;

    private final VkPhysicalDeviceFeatures features;
    private final PointerBuffer extensions;

    private boolean failed;

    /**
     * Creates new wrapper, fetch properties and extensions. getPhysicalDevice should be used to rate all graphic devices and get the best match
     *
     * @param stack    memory stack
     * @param instance vulkan instance
     * @param pointer  pointer to VkPhysicalDevice enumerated by vkEnumeratePhysicalDevices
     */
    public PhysicalDevice(MemoryStack stack, Instance instance, long pointer) {
        handle = new VkPhysicalDevice(pointer, instance.getHandle());

        // Get physical device properties
        properties = VkPhysicalDeviceProperties.malloc(stack);
        memoryProperties = VkPhysicalDeviceMemoryProperties.malloc(stack);
        VK10.vkGetPhysicalDeviceProperties(handle, properties);
        VK10.vkGetPhysicalDeviceMemoryProperties(handle, memoryProperties);

        Logger.debug("Found physical device type: {} vendor: {} api: {}", getDeviceType(properties), getVendor(properties), getApiVersion(properties));

        // Get physical device available features
        VkPhysicalDeviceFeatures features = VkPhysicalDeviceFeatures.malloc(stack);
        VK10.vkGetPhysicalDeviceFeatures(handle, features);

        // Uncomment if needed
        // Check if features are available and activate them
        //List<String> failedFeatures = new ArrayList<>();
        this.features = VkPhysicalDeviceFeatures.calloc(stack);
        //if(!features.shaderClipDistance()) {
        //    failedFeatures.add("shaderClipDistance");
        //}

/*        if(!failedFeatures.isEmpty()) {
            failed = true;
            Logger.debug("Failed to find features for physical device: ");
            for(String feature : failedFeatures) {
                Logger.debug(feature);
            }
        }*/

        //this.features.shaderClipDistance(true);

        // Get physical device available extensions
        IntBuffer pAvailableExtensionCount = stack.mallocInt(1);
        Check.vkCheck(VK10.vkEnumerateDeviceExtensionProperties(handle, (String) null, pAvailableExtensionCount, null), "Failed to get physical device extension count");
        int availableExtensionCount = pAvailableExtensionCount.get(0);

        // Get physical device extensions
        VkExtensionProperties.Buffer pAvailableExtensions = VkExtensionProperties.malloc(availableExtensionCount, stack);
        Check.vkCheck(VK10.vkEnumerateDeviceExtensionProperties(handle, (String) null, pAvailableExtensionCount, pAvailableExtensions), "Failed to get physical device extensions");

        Logger.debug("Found {} device extensions", availableExtensionCount);
        if (Logger.isTraceEnabled()) {
            for (int i = 0; i < availableExtensionCount; i++) {
                Logger.trace(pAvailableExtensions.get(i).extensionNameString());
            }
        }

        extensions = stack.mallocPointer(availableExtensionCount);

        if (!failed) {
            // Check if all required extensions are available
            List<String> failedToFind = new ArrayList<>();
            for (String requiredExtension : requiredExtensions) {
                boolean found = false;

                for (int i = 0; i < availableExtensionCount; i++) {
                    if (requiredExtension.equals(pAvailableExtensions.get(i).extensionNameString())) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    failedToFind.add(requiredExtension);
                    break;
                }

                extensions.put(stack.ASCII(requiredExtension));
            }

            if (!failedToFind.isEmpty()) {
                failed = true;
                Logger.debug("Failed to find extensions for physical device: ");
                for (String failed : failedToFind) {
                    Logger.debug(failed);
                }
            }

            extensions.flip(); // Don't know why needed but else SEGMENTATION_FAULT
        }
    }

    /**
     * Returns the best graphic device in your computer.
     *
     * @param stack    memory stack
     * @param instance vulkan instance
     * @return graphic device with most power
     */
    public static PhysicalDevice getPhysicalDevice(MemoryStack stack, Instance instance) {
        // Get physical devices count
        IntBuffer pDeviceCount = stack.mallocInt(1);
        Check.vkCheck(VK10.vkEnumeratePhysicalDevices(instance.getHandle(), pDeviceCount, null), "Failed to get physical device count");
        int deviceCount = pDeviceCount.get(0);

        // Get physical devices
        PointerBuffer pDevices = stack.mallocPointer(deviceCount);
        Check.vkCheck(VK10.vkEnumeratePhysicalDevices(instance.getHandle(), pDeviceCount, pDevices), "Failed to get physical devices");

        SortedMap<Integer, PhysicalDevice> devices = new TreeMap<>();

        // Create and rate physical devices for usability
        for (int i = 0; i < deviceCount; i++) {
            PhysicalDevice physicalDevice = new PhysicalDevice(stack, instance, pDevices.get(i));

            // Device failed to meet basic requirements like required extensions
            if (physicalDevice.isFailed()) {
                break;
            }

            int score = ratePhysicalDevice(stack, physicalDevice);

            // If score < 0 then device is not usable
            if (score < 0) {
                break;
            }

            devices.put(score, physicalDevice);
        }

        if (devices.isEmpty()) {
            throw new RuntimeException("Failed to find capable vulkan graphic device");
        }

        return devices.get(devices.firstKey());
    }

    /**
     * Rate properties of graphics device
     *
     * @param stack  memory stack
     * @param device vulkan instance
     * @return score
     */
    private static int ratePhysicalDevice(MemoryStack stack, PhysicalDevice device) {
        int score = 0;

        // Dedicated gpu have usually a much higher performance
        if (device.properties.deviceType() == VK10.VK_PHYSICAL_DEVICE_TYPE_DISCRETE_GPU) {
            score += 1000;
        }

        return score;
    }

    private static String getDeviceType(VkPhysicalDeviceProperties properties) {
        switch (properties.deviceType()) {
            case VK10.VK_PHYSICAL_DEVICE_TYPE_INTEGRATED_GPU:
                return "integrated";
            case VK10.VK_PHYSICAL_DEVICE_TYPE_DISCRETE_GPU:
                return "discrete";
            case VK10.VK_PHYSICAL_DEVICE_TYPE_VIRTUAL_GPU:
                return "virtual";
            case VK10.VK_PHYSICAL_DEVICE_TYPE_CPU:
                return "cpu";
            default:
                return "other " + properties.deviceType();
        }
    }

    private static String getVendor(VkPhysicalDeviceProperties properties) {
        switch (properties.vendorID()) {
            case 0x8086:
                return "intel";
            case 0x10DE:
                return "nvidia";
            case 0x1002:
                return "amd";
            default:
                return "other " + properties.vendorID();
        }
    }

    private static String getApiVersion(VkPhysicalDeviceProperties properties) {
        return VK10.VK_VERSION_MAJOR(properties.apiVersion()) + "." + VK10.VK_VERSION_MINOR(properties.apiVersion()) + "." + VK10.VK_VERSION_PATCH(properties.apiVersion());
    }

    /**
     * @return internal vulkan physical device handle
     */
    public VkPhysicalDevice getHandle() {
        return handle;
    }

    /**
     * @return device properties
     * @see VkPhysicalDeviceProperties
     */
    public VkPhysicalDeviceProperties getProperties() {
        return properties;
    }

    /**
     * @return device memory properties
     * @see VkPhysicalDeviceMemoryProperties
     */
    public VkPhysicalDeviceMemoryProperties getMemoryProperties() {
        return memoryProperties;
    }

    /**
     * @return available and required device features
     * @see VkPhysicalDeviceFeatures
     */
    public VkPhysicalDeviceFeatures getFeatures() {
        return features;
    }

    /**
     * @return array pointer to available and required extension names
     */
    public PointerBuffer getExtensions() {
        return extensions;
    }

    /**
     * @return true if device is not suitable
     */
    public boolean isFailed() {
        return failed;
    }
}
