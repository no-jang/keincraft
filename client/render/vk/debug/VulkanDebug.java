package client.render.vk.debug;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.VkDebugReportCallbackCreateInfoEXT;
import org.lwjgl.vulkan.VkDebugReportCallbackEXT;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.EXTDebugReport.*;
import static org.lwjgl.vulkan.VK10.*;

public final class VulkanDebug {
    public static VkDebugReportCallbackCreateInfoEXT createDebugCallback(MemoryStack stack, VkInstanceCreateInfo instanceCreateInfo) {
        if (!VulkanValidation.validationLayersEnabled) return null;

        VkDebugReportCallbackEXT debugCallbackFunction = VkDebugReportCallbackEXT.create(
                (flags, objectType, object, location, messageCode, pLayerPrefix, pMessage, pUserData) -> {
                    String type;
                    if ((flags & VK_DEBUG_REPORT_INFORMATION_BIT_EXT) != 0) {
                        type = "INFORMATION";
                    } else if ((flags & VK_DEBUG_REPORT_WARNING_BIT_EXT) != 0) {
                        type = "WARNING";
                    } else if ((flags & VK_DEBUG_REPORT_PERFORMANCE_WARNING_BIT_EXT) != 0) {
                        type = "PERFORMANCE WARNING";
                    } else if ((flags & VK_DEBUG_REPORT_ERROR_BIT_EXT) != 0) {
                        type = "ERROR";
                    } else if ((flags & VK_DEBUG_REPORT_DEBUG_BIT_EXT) != 0) {
                        type = "DEBUG";
                    } else {
                        type = "UNKNOWN";
                    }

                    String layerPrefix = MemoryUtil.memASCII(pLayerPrefix);
                    String message = VkDebugReportCallbackEXT.getString(pMessage);

                    System.err.println("validation layer " + type + " " + layerPrefix + ": " + messageCode + message);

                    return VK_FALSE;
                });

        VkDebugReportCallbackCreateInfoEXT createInfo = VkDebugReportCallbackCreateInfoEXT.mallocStack(stack)
                .sType(VK_STRUCTURE_TYPE_DEBUG_REPORT_CREATE_INFO_EXT)
                .pNext(MemoryUtil.NULL)
                .flags(VK_DEBUG_REPORT_ERROR_BIT_EXT | VK_DEBUG_REPORT_WARNING_BIT_EXT)
                .pfnCallback(debugCallbackFunction)
                .pUserData(MemoryUtil.NULL);

        instanceCreateInfo.pNext(createInfo.address());

        return createInfo;
    }

    public static VkDebugReportCallbackEXT setupDebugCallback(MemoryStack stack, VkInstance instance, VkDebugReportCallbackCreateInfoEXT createInfo) {
        if (!VulkanValidation.validationLayersEnabled) return null;

        LongBuffer debugReportCallback = stack.mallocLong(1);
        vkCheck(vkCreateDebugReportCallbackEXT(instance, createInfo, null, debugReportCallback), "Failed to create debug report callback");

        return VkDebugReportCallbackEXT.create(debugReportCallback.get());
    }

    public static void destroyDebugCallback(VkInstance instance, VkDebugReportCallbackEXT debugReportCallback) {
        if (!VulkanValidation.validationLayersEnabled) return;

        vkDestroyDebugReportCallbackEXT(instance, debugReportCallback.address(), null);
    }

    public static String getErrorMessage(int result) {
        switch (result) {
            case VK_ERROR_DEVICE_LOST:
                return "lost device";
            case VK_ERROR_EXTENSION_NOT_PRESENT:
                return "could not find extension";
            case VK_ERROR_FEATURE_NOT_PRESENT:
                return "could not find feature";
            case VK_ERROR_FRAGMENTED_POOL:
                return "fragmented pool";
            case VK_ERROR_FORMAT_NOT_SUPPORTED:
                return "format not supported";
            case VK_ERROR_INCOMPATIBLE_DRIVER:
                return "incompatible driver";
            case VK_ERROR_INITIALIZATION_FAILED:
                return "initialization failed";
            case VK_ERROR_LAYER_NOT_PRESENT:
                return "could not find layer";
            case VK_ERROR_MEMORY_MAP_FAILED:
                return "memory map failed";
            case VK_ERROR_OUT_OF_DEVICE_MEMORY:
                return "out of device memory";
            case VK_ERROR_OUT_OF_HOST_MEMORY:
                return "out of host memory";
            case VK_ERROR_TOO_MANY_OBJECTS:
                return "too many objects";
            default:
                return "unknown error";
        }
    }

    public static void vkCheck(int result, String message) {
        if (result != VK_SUCCESS) {
            throw new RuntimeException(message + ": " + getErrorMessage(result));
        }
    }
}
