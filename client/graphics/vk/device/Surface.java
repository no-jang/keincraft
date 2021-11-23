package client.graphics.vk.device;

import client.graphics.Window;
import client.graphics.vk.util.Check;
import org.lwjgl.glfw.GLFWVulkan;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.KHRSurface;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkSurfaceCapabilitiesKHR;
import org.lwjgl.vulkan.VkSurfaceFormatKHR;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.Objects;

/**
 * Surface to present rendered images. For example a window
 */
public class Surface {
    private final long handle;
    private final VkSurfaceCapabilitiesKHR capabilities;
    private final VkSurfaceFormatKHR format;
    private final int presentMode;

    /**
     * Creates new surface for a window and a physical device. Gathers properties about the surface
     *
     * @param stack    memory stack
     * @param instance vulkan instance
     * @param device   physical device that will render the image
     * @param window   window the surface will represent
     */
    public Surface(MemoryStack stack, Instance instance, PhysicalDevice device, Window window) {
        // Create surface for window
        LongBuffer pSurface = stack.mallocLong(1);
        Check.vkCheck(GLFWVulkan.glfwCreateWindowSurface(instance.getHandle(), window.getHandle(), null, pSurface), "Failed to create window surface");
        handle = pSurface.get(0);

        // Get surface capabilities properties
        capabilities = VkSurfaceCapabilitiesKHR.malloc(stack);
        Check.vkCheck(KHRSurface.vkGetPhysicalDeviceSurfaceCapabilitiesKHR(device.getHandle(), handle, capabilities), "Failed to get window surface capabilities");

        // Get formats supported by the surface
        IntBuffer pFormatCount = stack.mallocInt(1);
        Check.vkCheck(KHRSurface.vkGetPhysicalDeviceSurfaceFormatsKHR(device.getHandle(), handle, pFormatCount, null), "Failed to get available window surface format count");
        int formatCount = pFormatCount.get(0);

        VkSurfaceFormatKHR.Buffer pFormats = VkSurfaceFormatKHR.malloc(formatCount, stack);
        Check.vkCheck(KHRSurface.vkGetPhysicalDeviceSurfaceFormatsKHR(device.getHandle(), handle, pFormatCount, pFormats), "Failed to get available window surface formats");

        // Check if surface meets standards or use the first we find
        VkSurfaceFormatKHR foundFormat = null;
        for (int i = 0; i < formatCount; i++) {
            VkSurfaceFormatKHR format = pFormats.get(i);
            if (format.format() == VK10.VK_FORMAT_B8G8R8A8_SRGB && format.colorSpace() == KHRSurface.VK_COLOR_SPACE_SRGB_NONLINEAR_KHR) {
                foundFormat = format;
                break;
            }
        }

        format = Objects.requireNonNullElseGet(foundFormat, () -> pFormats.get(0));

        // Get present modes supported by the surface
        IntBuffer pPresentModeCount = stack.mallocInt(1);
        Check.vkCheck(KHRSurface.vkGetPhysicalDeviceSurfacePresentModesKHR(device.getHandle(), handle, pPresentModeCount, null), "Failed to get available window surface present mode count");
        int presentModeCount = pPresentModeCount.get(0);

        IntBuffer pPresentModes = stack.mallocInt(presentModeCount);
        Check.vkCheck(KHRSurface.vkGetPhysicalDeviceSurfacePresentModesKHR(device.getHandle(), handle, pPresentModeCount, pPresentModes), "Failed to get available window surface present modes");

        // Check if mailbox present mode is supported or use fifo present mode
        int foundPresentMode = -1;
        for (int i = 0; i < presentModeCount; i++) {
            int presentMode = pPresentModes.get(i);
            if (presentMode == KHRSurface.VK_PRESENT_MODE_MAILBOX_KHR) {
                foundPresentMode = presentMode;
            }
        }

        if(foundPresentMode == -1) {
            presentMode = KHRSurface.VK_PRESENT_MODE_FIFO_KHR;
        } else {
            presentMode = foundPresentMode;
        }
    }

    /**
     * Destroys the surface instance
     *
     * @param instance vulkan instance
     */
    public void destroy(Instance instance) {
        KHRSurface.vkDestroySurfaceKHR(instance.getHandle(), handle, null);
    }

    /**
     * Gets internal vulkan handle
     *
     * @return internal vulkan handle
     */
    public long getHandle() {
        return handle;
    }

    /**
     * Gets surface capabilities
     *
     * @return properties of the surface
     * @see VkSurfaceCapabilitiesKHR
     */
    public VkSurfaceCapabilitiesKHR getCapabilities() {
        return capabilities;
    }

    /**
     * Gets surface format
     *
     * @return found format of the surface
     * @see VkSurfaceFormatKHR
     */
    public VkSurfaceFormatKHR getFormat() {
        return format;
    }

    /**
     * Gets present mode of the surface
     *
     * @return found present mode of the surface
     */
    public int getPresentMode() {
        return presentMode;
    }
}
