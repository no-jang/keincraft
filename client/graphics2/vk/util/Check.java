package client.graphics2.vk.util;

import org.lwjgl.vulkan.KHRSurface;
import org.lwjgl.vulkan.KHRSwapchain;

import static org.lwjgl.vulkan.VK10.VK_ERROR_DEVICE_LOST;
import static org.lwjgl.vulkan.VK10.VK_ERROR_EXTENSION_NOT_PRESENT;
import static org.lwjgl.vulkan.VK10.VK_ERROR_FEATURE_NOT_PRESENT;
import static org.lwjgl.vulkan.VK10.VK_ERROR_FORMAT_NOT_SUPPORTED;
import static org.lwjgl.vulkan.VK10.VK_ERROR_FRAGMENTED_POOL;
import static org.lwjgl.vulkan.VK10.VK_ERROR_INCOMPATIBLE_DRIVER;
import static org.lwjgl.vulkan.VK10.VK_ERROR_INITIALIZATION_FAILED;
import static org.lwjgl.vulkan.VK10.VK_ERROR_LAYER_NOT_PRESENT;
import static org.lwjgl.vulkan.VK10.VK_ERROR_MEMORY_MAP_FAILED;
import static org.lwjgl.vulkan.VK10.VK_ERROR_OUT_OF_DEVICE_MEMORY;
import static org.lwjgl.vulkan.VK10.VK_ERROR_OUT_OF_HOST_MEMORY;
import static org.lwjgl.vulkan.VK10.VK_ERROR_TOO_MANY_OBJECTS;
import static org.lwjgl.vulkan.VK10.VK_SUCCESS;

public final class Check {
    /**
     * Returns human-readable error message from vulkan error code
     *
     * @param result vulkan error code
     * @return human-readable message
     */
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
            case KHRSurface.VK_ERROR_NATIVE_WINDOW_IN_USE_KHR:
                return "native window in use";
            default:
                return "unknown error: " + result;
        }
    }

    /**
     * Check if vulkan return code != VK_SUCCESS and then throw an exception with message and vulkan error code translated to human-readable error.
     *
     * @param result  vulkan return code
     * @param message message for the exception
     */
    public static void vkCheck(int result, String message) {
        if (result != VK_SUCCESS) {
            throw new RuntimeException(message + ": " + getErrorMessage(result));
        }
    }

    /**
     * Check if vulkan error VK_ERROR_OUT_OF_DATE_KHR or VK_SUBOPTIMAL_KHR is returned, so swapchain size has been changed
     * If there is another code not VK_SUCCESS an exception with message and human-readable vulkan error code will be thrown
     *
     * @param result  vulkan return code
     * @param message message for the exception
     * @return true if result code equals VK_ERROR_OUT_OF_DATE_KHR or VK_SUBOPTIMAL_KHR and so the swapchain size has been changed
     */
    public static boolean vkCheckResized(int result, String message) {
        if (result == KHRSwapchain.VK_ERROR_OUT_OF_DATE_KHR || result == KHRSwapchain.VK_SUBOPTIMAL_KHR) {
            return true;
        }

        vkCheck(result, message);
        return false;
    }
}
