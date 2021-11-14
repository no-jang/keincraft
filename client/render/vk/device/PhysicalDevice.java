package client.render.vk.device;

import client.render.vk.device.queue.QueueFamilies;
import client.render.vk.present.PresentMode;
import client.render.vk.present.Surface;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static client.render.vk.Global.vkCheck;
import static org.lwjgl.vulkan.KHRSurface.*;
import static org.lwjgl.vulkan.VK10.*;

public class PhysicalDevice {
    private static final String[] requiredDeviceExtensionNames = new String[]{
            KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME
    };

    private final VkPhysicalDevice handle;
    private final int score;

    private QueueFamilies queueFamilies;

    private PointerBuffer requiredExtensions;
    private VkPhysicalDeviceFeatures requiredFeatures;
    private VkPhysicalDeviceProperties properties;
    private VkSurfaceCapabilitiesKHR surfaceCapabilities;
    private List<VkSurfaceFormatKHR> surfaceFormats;
    private List<PresentMode> surfacePresentModes;

    public PhysicalDevice(MemoryStack stack, Instance instance, long aPhysicalDevice, Surface surface) {
        handle = new VkPhysicalDevice(aPhysicalDevice, instance.getHandle());
        score = checkDevice(stack, surface);
    }

    public static PhysicalDevice pickPhysicalDevice(MemoryStack stack, Instance instance, Surface surface) {
        // Enumerate physical device count which supports vulkan
        IntBuffer pPhysicalDeviceCount = stack.mallocInt(1);
        vkCheck(vkEnumeratePhysicalDevices(instance.getHandle(), pPhysicalDeviceCount, null), "Failed to enumerate physical device count");
        int physicalDeviceCount = pPhysicalDeviceCount.get(0);
        pPhysicalDeviceCount.position(0);

        if (physicalDeviceCount == 0) {
            throw new RuntimeException("Failed to find graphics device that supports vulkan");
        }

            SortedMap<Integer, PhysicalDevice> physicalDevices = new TreeMap<>();

            PointerBuffer pPhysicalDevices = stack.mallocPointer(physicalDeviceCount);
            vkCheck(vkEnumeratePhysicalDevices(instance.getHandle(), pPhysicalDeviceCount, pPhysicalDevices), "Failed to enumerate physical devices");
            for (int physicalDeviceIndex = 0; physicalDeviceIndex < pPhysicalDevices.capacity(); physicalDeviceIndex++) {
                PhysicalDevice physicalDevice = new PhysicalDevice(stack, instance, pPhysicalDevices.get(physicalDeviceIndex), surface);

                // Rate every physical device based on its features
                physicalDevices.put(physicalDevice.getScore(), physicalDevice);
            }

            int bestDeviceScore = physicalDevices.firstKey();
            if (bestDeviceScore < 0) {
                throw new RuntimeException("No suitable graphics device found");
            }

            return physicalDevices.get(bestDeviceScore);
    }

    private int checkDevice(MemoryStack stack, Surface surface) {
        int score = 0;

        int extensionScore = checkDeviceExtensions(stack);
        if (extensionScore < 0) {
            return -1;
        }
        score += extensionScore;

        int featureScore = checkDeviceFeatures(stack);
        if (featureScore < 0) {
            return -1;
        }
        score += featureScore;

        int propertiesScore = checkDeviceProperties(stack);
        if (propertiesScore < 0) {
            return -1;
        }
        score += propertiesScore;

        int queueFamiliesScore = checkQueueFamilies(stack, surface);
        if (queueFamiliesScore < 0) {
            return -1;
        }
        score += queueFamiliesScore;

        int swapChainScore = checkSwapChain(stack, surface);
        if (swapChainScore < 0) {
            return -1;
        }
        score += swapChainScore;

        return score;
    }

    private int checkDeviceExtensions(MemoryStack stack) {
        // Enumerate physical device extension count
        IntBuffer pDeviceExtensionCount = stack.mallocInt(1);
        vkCheck(vkEnumerateDeviceExtensionProperties(handle, (String) null, pDeviceExtensionCount, null), "Failed to enumerate physical device extension count");
        int deviceExtensionCount = pDeviceExtensionCount.get(0);

        // Enumerate physical device extensions
        VkExtensionProperties.Buffer pDeviceExtensions = VkExtensionProperties.malloc(deviceExtensionCount, stack);
        vkCheck(vkEnumerateDeviceExtensionProperties(handle, (String) null, pDeviceExtensionCount, pDeviceExtensions), "Failed to enumerate physical device extensions");

        List<String> deviceExtensions = new ArrayList<>(deviceExtensionCount);

        // Gather device extension names
        for (int deviceExtensionIndex = 0; deviceExtensionIndex < deviceExtensionCount; deviceExtensionIndex++) {
            deviceExtensions.add(pDeviceExtensions.get(deviceExtensionIndex).extensionNameString());
        }

        // Check if all required extensions are available
        requiredExtensions = stack.mallocPointer(requiredDeviceExtensionNames.length);
        for (String requiredDeviceExtension : requiredDeviceExtensionNames) {
            if (!deviceExtensions.contains(requiredDeviceExtension)) {
                return -1; // Return device is not suitable
            }

            requiredExtensions.put(stack.ASCII(requiredDeviceExtension));
        }

        requiredExtensions.flip();

        // If optional extensions are needed the score can be returned here
        return 0;
    }

    private int checkDeviceFeatures(MemoryStack stack) {
        //Get physical device features
        VkPhysicalDeviceFeatures physicalDeviceFeatures = VkPhysicalDeviceFeatures.malloc(stack);
        vkGetPhysicalDeviceFeatures(handle, physicalDeviceFeatures);

        requiredFeatures = VkPhysicalDeviceFeatures.calloc(stack);

        // IMPORTANT check if feature is available at physical device and activate it through requiredFeatures
        if (!physicalDeviceFeatures.shaderClipDistance()) {
            return -1;
        }

        requiredFeatures.shaderClipDistance(true);

        // If optional features are needed the score can be returned here
        return 0;
    }

    private int checkDeviceProperties(MemoryStack stack) {
        VkPhysicalDeviceProperties physicalDeviceProperties = VkPhysicalDeviceProperties.malloc(stack);
        vkGetPhysicalDeviceProperties(handle, physicalDeviceProperties);

        int score = 0;
        if (physicalDeviceProperties.deviceType() == VK_PHYSICAL_DEVICE_TYPE_DISCRETE_GPU) {
            score += 1000;
        }

        properties = physicalDeviceProperties;
        return score;
    }

    private int checkQueueFamilies(MemoryStack stack, Surface surface) {
        queueFamilies = new QueueFamilies(stack, this, surface);

        if (!queueFamilies.isSuitable()) {
            return -1;
        }

        return 0;
    }

    private int checkSwapChain(MemoryStack stack, Surface surface) {
        surfaceCapabilities = VkSurfaceCapabilitiesKHR.malloc(stack);
        vkCheck(vkGetPhysicalDeviceSurfaceCapabilitiesKHR(handle, surface.getHandle(), surfaceCapabilities),
                "Failed to get physical device surface capabilities");

        IntBuffer pFormatsCount = stack.mallocInt(1);
        vkCheck(vkGetPhysicalDeviceSurfaceFormatsKHR(handle, surface.getHandle(), pFormatsCount, null),
                "Failed to get physical device surface format count");
        int formatCounts = pFormatsCount.get(0);

        VkSurfaceFormatKHR.Buffer pFormats = VkSurfaceFormatKHR.malloc(formatCounts, stack);
        vkCheck(vkGetPhysicalDeviceSurfaceFormatsKHR(handle, surface.getHandle(), pFormatsCount, pFormats),
                "Failed to get physical device surface formats");

        surfaceFormats = new ArrayList<>(formatCounts);
        for (int formatIndex = 0; formatIndex < formatCounts; formatIndex++) {
            surfaceFormats.add(pFormats.get(formatIndex));
        }

        IntBuffer pPresentModesCount = stack.mallocInt(1);
        vkCheck(vkGetPhysicalDeviceSurfacePresentModesKHR(handle, surface.getHandle(), pPresentModesCount, null),
                "Failed to get physical device surface present modes");
        int presentModesCount = pPresentModesCount.get(0);

        IntBuffer pPresentModes = stack.mallocInt(presentModesCount);
        vkCheck(vkGetPhysicalDeviceSurfacePresentModesKHR(handle, surface.getHandle(), pPresentModesCount, pPresentModes),
                "Failed to get physical device surface present modes");

        surfacePresentModes = PresentMode.fromBuffer(pPresentModes);

        if (surfaceFormats.isEmpty() || surfacePresentModes.isEmpty()) {
            return -1;
        }

        return 0;
    }

    public VkPhysicalDevice getHandle() {
        return handle;
    }

    public int getScore() {
        return score;
    }

    public PointerBuffer getRequiredExtensions() {
        return requiredExtensions;
    }

    public VkPhysicalDeviceFeatures getRequiredFeatures() {
        return requiredFeatures;
    }

    public VkPhysicalDeviceProperties getProperties() {
        return properties;
    }

    public QueueFamilies getQueueFamilies() {
        return queueFamilies;
    }

    public VkSurfaceCapabilitiesKHR getSurfaceCapabilities() {
        return surfaceCapabilities;
    }

    public List<VkSurfaceFormatKHR> getSurfaceFormats() {
        return surfaceFormats;
    }

    public List<PresentMode> getSurfacePresentModes() {
        return surfacePresentModes;
    }
}
