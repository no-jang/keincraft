package client.graphics.vk.device;

import client.graphics.vk.util.Check;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.KHRSwapchain;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkExtensionProperties;
import org.lwjgl.vulkan.VkPhysicalDevice;
import org.lwjgl.vulkan.VkPhysicalDeviceFeatures;
import org.lwjgl.vulkan.VkPhysicalDeviceMemoryProperties;
import org.lwjgl.vulkan.VkPhysicalDeviceProperties;
import org.tinylog.Logger;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Physical graphics device that supports vulkan
 *
 * @see VkPhysicalDevice
 */
public class PhysicalDevice {
    private static final String[] REQUIRED_EXTENSIONS = new String[]{
            KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME
    };

    private final VkPhysicalDevice handle;
    private final VkPhysicalDeviceProperties properties;
    private final VkPhysicalDeviceMemoryProperties memoryProperties;
    private final VkPhysicalDeviceFeatures availableFeatures;
    private final List<String> availableExtensions;

    private final VkPhysicalDeviceFeatures enabledFeatures;
    private final List<String> enabledExtensions;

    /**
     * Creates new wrapper, fetch device properties and extensions.
     * {@link #getBestPhysicalDevice(MemoryStack, Instance)} should be used to get the best suitable physical device
     *
     * @param stack          memory stack
     * @param instance       vulkan instance
     * @param internalHandle internal handle of VkPhysicalDevice enumerated by vkEnumeratePhysicalDevices
     */
    public PhysicalDevice(MemoryStack stack, Instance instance, long internalHandle) {
        handle = new VkPhysicalDevice(internalHandle, instance.getHandle());

        // Gather device properties
        properties = VkPhysicalDeviceProperties.malloc(stack);
        memoryProperties = VkPhysicalDeviceMemoryProperties.malloc(stack);
        VK10.vkGetPhysicalDeviceProperties(handle, properties);
        VK10.vkGetPhysicalDeviceMemoryProperties(handle, memoryProperties);

        // Gather device features
        availableFeatures = VkPhysicalDeviceFeatures.malloc(stack);
        VK10.vkGetPhysicalDeviceFeatures(handle, availableFeatures);

        enabledFeatures = VkPhysicalDeviceFeatures.calloc(stack);

        // Gather extensions
        IntBuffer pExtensionCount = stack.mallocInt(1);
        Check.vkCheck(VK10.vkEnumerateDeviceExtensionProperties(handle, (String) null, pExtensionCount, null), "Failed to get device extension count");
        int extensionCount = pExtensionCount.get(0);

        VkExtensionProperties.Buffer pExtensions = VkExtensionProperties.malloc(extensionCount, stack);
        Check.vkCheck(VK10.vkEnumerateDeviceExtensionProperties(handle, (String) null, pExtensionCount, pExtensions), "Failed to get device extensions");

        availableExtensions = new ArrayList<>(extensionCount);
        for (int i = 0; i < extensionCount; i++) {
            availableExtensions.add(pExtensions.get(i).extensionNameString());
        }

        enabledExtensions = new ArrayList<>(extensionCount);

        // Log device details
        if (Logger.isDebugEnabled()) {
            Logger.debug("Found physical device type: {} vendor: {} api: {}", getDeviceType(properties), getVendor(properties), getApiVersion(properties));
            Logger.debug("Found {} device extensions", extensionCount);

            if (Logger.isTraceEnabled()) {
                StringBuilder builder = new StringBuilder();
                for (String extension : availableExtensions) {
                    builder.append(extension);
                    builder.append(System.lineSeparator());
                }
                Logger.trace(builder.toString());
            }
        }
    }

    /**
     * Gathers all physical devices with properties
     *
     * @param stack    memory stack
     * @param instance instance
     * @return list of all physical devices
     */
    public static List<PhysicalDevice> getPhysicalDevices(MemoryStack stack, Instance instance) {
        // Get available physical devices
        IntBuffer pDeviceCount = stack.mallocInt(1);
        Check.vkCheck(VK10.vkEnumeratePhysicalDevices(instance.getHandle(), pDeviceCount, null), "Failed to get physical device count");
        int deviceCount = pDeviceCount.get(0);

        PointerBuffer pDevices = stack.mallocPointer(deviceCount);
        Check.vkCheck(VK10.vkEnumeratePhysicalDevices(instance.getHandle(), pDeviceCount, pDevices), "Failed to get physical devices");

        // Create physical device wrappers and gather device properties
        List<PhysicalDevice> devices = new ArrayList<>(deviceCount);
        for (int i = 0; i < deviceCount; i++) {
            devices.add(new PhysicalDevice(stack, instance, pDevices.get(i)));
        }

        return devices;
    }

    /**
     * Gathers all physical devices and rates them. The device with the best score gets returned and is considered the most suitable device.
     * Devices with a score of -1 do not meet the requirements of features or extensions
     *
     * @param stack    memory stack
     * @param instance vulkan instance
     * @return most suitable device
     */
    public static PhysicalDevice getBestPhysicalDevice(MemoryStack stack, Instance instance) {
        List<PhysicalDevice> physicalDevices = getPhysicalDevices(stack, instance);
        SortedMap<Integer, PhysicalDevice> ratedPhysicalDevices = new TreeMap<>();

        // Rate all found physical devices
        for (PhysicalDevice physicalDevice : physicalDevices) {
            ratedPhysicalDevices.put(ratePhysicalDevice(physicalDevice), physicalDevice);
        }

        // If all score are lower than 0, no device was found with all required properties
        int firstKey = ratedPhysicalDevices.firstKey();
        if (firstKey < 0) {
            throw new RuntimeException("Failed to find a suitable and vulkan capable graphic device");
        }

        return ratedPhysicalDevices.get(firstKey);
    }

    /**
     * Rates a physical device. -1 means the device is for this application unsuited
     *
     * @param device vulkan device
     * @return score of the device
     */
    private static int ratePhysicalDevice(PhysicalDevice device) {
        int score = 0;

        // Dedicated gpus have usually a much higher performance
        if (device.properties.deviceType() == VK10.VK_PHYSICAL_DEVICE_TYPE_DISCRETE_GPU) {
            score += 1000;
        }

        // Check if all required extensions are available
        for (String requiredExtension : REQUIRED_EXTENSIONS) {
            if (!device.getAvailableExtensions().contains(requiredExtension)) {
                Logger.debug("Failed to find required extension {} for device", requiredExtension);
                return -1;
            }

            // Add extension to the required to be enabled ones
            device.getEnabledExtensions().add(requiredExtension);
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
     * Gets internal vulkan handle
     *
     * @return internal vulkan handle
     * @see VkPhysicalDevice
     */
    public VkPhysicalDevice getHandle() {
        return handle;
    }

    /**
     * Gets device properties such a vendor, type, api version, ...
     *
     * @return device properties
     * @see VkPhysicalDeviceProperties
     */
    public VkPhysicalDeviceProperties getProperties() {
        return properties;
    }

    /**
     * Gets memory properties of device
     *
     * @return memory properties of device
     * @see VkPhysicalDeviceMemoryProperties
     */
    public VkPhysicalDeviceMemoryProperties getMemoryProperties() {
        return memoryProperties;
    }

    /**
     * Gets all available features of this device
     *
     * @return available device features
     * @see VkPhysicalDeviceFeatures
     */
    public VkPhysicalDeviceFeatures getAvailableFeatures() {
        return availableFeatures;
    }

    /**
     * Gets all available extension names of device
     *
     * @return list of available device extension names
     */
    public List<String> getAvailableExtensions() {
        return availableExtensions;
    }

    /**
     * Gets a VkPhysicalDeviceFeatures object where enable device features can be selected.
     * <p>
     * It is allocated with the MemoryStack provided in the constructor!
     *
     * @return VkPhysicalDeviceFeatures object
     * @see VkPhysicalDeviceFeatures
     */
    public VkPhysicalDeviceFeatures getEnabledFeatures() {
        return enabledFeatures;
    }

    /**
     * Gets a List with enabled device extension names where you can enable extensions
     *
     * @return list with enable device extension names
     */
    public List<String> getEnabledExtensions() {
        return enabledExtensions;
    }
}
