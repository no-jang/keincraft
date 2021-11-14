package client.render.vk.present;

import client.render.Window;
import client.render.vk.device.Device;
import client.render.vk.device.PhysicalDevice;
import common.util.math.Math;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.LongBuffer;
import java.util.List;

import static client.render.vk.Global.vkCheck;
import static org.lwjgl.vulkan.VK10.*;

public class Swapchain {
    private final long handle;
    private final VkSurfaceFormatKHR format;
    private final VkExtent2D extent;

    private int imageCount;

    public Swapchain(MemoryStack stack, PhysicalDevice physicalDevice, Device device, Surface surface, Window window) {
        VkSurfaceCapabilitiesKHR capabilities = physicalDevice.getSurfaceCapabilities();

        format = chooseSurfaceFormat(physicalDevice.getSurfaceFormats());
        PresentMode presentMode = choosePresentMode(physicalDevice.getSurfacePresentModes());
        extent = chooseExtent(stack, window, capabilities);

        // Swap chain image count, +1 because we don't want to wait for the driver before starting next frame
        imageCount = capabilities.minImageCount() + 1;

        if (capabilities.maxImageCount() > 0 && imageCount > capabilities.maxImageCount()) {
            imageCount = capabilities.maxImageCount();
            }

            VkSwapchainCreateInfoKHR createInfo = VkSwapchainCreateInfoKHR.malloc(stack)
                    .sType$Default()
                    .surface(surface.getHandle())
                    .minImageCount(imageCount)
                    .imageFormat(format.format())
                    .imageColorSpace(format.colorSpace())
                    .imageExtent(extent)
                    .imageArrayLayers(1)
                    .imageUsage(VK_IMAGE_USAGE_COLOR_ATTACHMENT_BIT);

            createInfo.imageSharingMode(VK_SHARING_MODE_EXCLUSIVE);
            createInfo.queueFamilyIndexCount(0);
            createInfo.pQueueFamilyIndices(null);

            createInfo.preTransform(capabilities.currentTransform());
            createInfo.compositeAlpha(KHRSurface.VK_COMPOSITE_ALPHA_OPAQUE_BIT_KHR);
            createInfo.presentMode(presentMode.getIndex());
            createInfo.clipped(true);
            createInfo.oldSwapchain(VK_NULL_HANDLE);
            createInfo.pNext(VK_NULL_HANDLE);
            createInfo.flags(0);

            LongBuffer pSwapChain = stack.mallocLong(1);
            vkCheck(KHRSwapchain.vkCreateSwapchainKHR(device.getHandle(), createInfo, null, pSwapChain), "Failed to create swapChain");
            handle = pSwapChain.get(0);
    }

    public int getImageCount() {
        return imageCount;
    }

    public VkSurfaceFormatKHR getFormat() {
        return format;
    }

    public VkExtent2D getExtent() {
        return extent;
    }

    public static VkSurfaceFormatKHR chooseSurfaceFormat(List<VkSurfaceFormatKHR> availableFormats) {
        for (VkSurfaceFormatKHR format : availableFormats) {
            if (format.format() == VK_FORMAT_B8G8R8A8_SRGB && format.colorSpace() == KHRSurface.VK_COLOR_SPACE_SRGB_NONLINEAR_KHR) {
                return format;
            }
        }

        return availableFormats.get(0);
    }

    public static PresentMode choosePresentMode(List<PresentMode> availablePresentModes) {
        for (PresentMode presentMode : availablePresentModes) {
            if (presentMode == PresentMode.MAILBOX) {
                return presentMode;
            }
        }

        return PresentMode.FIFO;
    }

    public static VkExtent2D chooseExtent(MemoryStack stack, Window window, VkSurfaceCapabilitiesKHR capabilities) {
        if (capabilities.currentExtent().width() != 0xFFFFFFFF) {
            return capabilities.currentExtent();
        } else {
            VkExtent2D actualExtent = VkExtent2D.malloc(stack)
                    .width(window.getWidth())
                    .height(window.getHeight());

            actualExtent.width(Math.clamp(actualExtent.width(), capabilities.minImageExtent().width(), capabilities.maxImageExtent().width()));
            actualExtent.height(Math.clamp(actualExtent.height(), capabilities.minImageExtent().height(), capabilities.maxImageExtent().height()));

            return actualExtent;
        }
    }

    public void destroy(Device device) {
        KHRSwapchain.vkDestroySwapchainKHR(device.getHandle(), handle, null);
    }

    public long getHandle() {
        return handle;
    }
}
