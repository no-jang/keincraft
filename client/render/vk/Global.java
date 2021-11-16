package client.render.vk;

import org.lwjgl.vulkan.KHRSwapchain;

import static org.lwjgl.vulkan.VK10.*;

public final class Global {
    public static final boolean isDebug = true;

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
            case KHRSwapchain.VK_ERROR_OUT_OF_DATE_KHR:
                return "swapchain is out of date";
            default:
                return "unknown error: " + result;
        }
    }

    public static void vkCheck(int result, String message) {
        if (result != VK_SUCCESS) {
            throw new RuntimeException(message + ": " + getErrorMessage(result));
        }
    }

    public static boolean vkCheckResized(int result, String message) {
        if (result == KHRSwapchain.VK_ERROR_OUT_OF_DATE_KHR || result == KHRSwapchain.VK_SUBOPTIMAL_KHR) {
            return true;
        }

        vkCheck(result, message);
        return false;
    }
}
