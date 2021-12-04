package client.graphics.vk.renderpass;

import client.graphics.Window;
import client.graphics.vk.device.Device;
import client.graphics.vk.device.Surface;
import client.graphics.vk.image.Image;
import client.graphics.vk.image.ImageView;
import client.graphics.vk.sync.Fence;
import client.graphics.vk.sync.Semaphore;
import client.graphics.vk.util.Check;
import common.util.math.MathUtil;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.KHRSurface;
import org.lwjgl.vulkan.KHRSwapchain;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkExtent2D;
import org.lwjgl.vulkan.VkSurfaceCapabilitiesKHR;
import org.lwjgl.vulkan.VkSwapchainCreateInfoKHR;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds all framebuffers the image is being rendered to before pushing them to screen
 */
public class Swapchain {
    private final long handle;
    private final int format;
    private final int imageCount;
    private final VkExtent2D extent;

    private final Map<Integer, Fence> fencesInFlight;

    /**
     * Creates new swapchain
     *
     * @param stack        memory stack
     * @param device       vulkan device
     * @param surface      surface
     * @param window       window
     * @param oldSwapchain if available old swapchain to speed up creation process
     */
    public Swapchain(MemoryStack stack, Device device, Surface surface, Window window, Swapchain oldSwapchain) {
        // Find suitable present mode and image count
        VkSurfaceCapabilitiesKHR capabilities = surface.getCapabilities();
        int presentMode = surface.getPresentMode();

        extent = chooseExtent(stack, window, capabilities);
        format = surface.getFormat().format();

        if (capabilities.maxImageCount() > 0 && capabilities.minImageCount() + 1 > capabilities.maxImageCount()) {
            imageCount = capabilities.maxImageCount();
        } else {
            imageCount = capabilities.minImageCount() + 1;
        }

        VkSwapchainCreateInfoKHR createInfo = VkSwapchainCreateInfoKHR.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .surface(surface.getHandle())
                .minImageCount(imageCount)
                .imageFormat(format)
                .imageColorSpace(surface.getFormat().colorSpace())
                .imageExtent(extent)
                .imageArrayLayers(1)
                .imageUsage(VK10.VK_IMAGE_USAGE_COLOR_ATTACHMENT_BIT)
                .preTransform(capabilities.currentTransform())
                .compositeAlpha(KHRSurface.VK_COMPOSITE_ALPHA_OPAQUE_BIT_KHR)
                .presentMode(presentMode)
                .clipped(true);

        // Make possible for if available to queue to use this swapchain
        if (device.getGraphicsQueueFamilyIndex() != device.getPresentQueueFamilyIndex()) {
            int[] queues = new int[]{device.getGraphicsQueueFamilyIndex(), device.getPresentQueueFamilyIndex()};
            createInfo
                    .imageSharingMode(VK10.VK_SHARING_MODE_CONCURRENT)
                    .queueFamilyIndexCount(queues.length)
                    .pQueueFamilyIndices(stack.ints(queues));
        } else {
            createInfo
                    .imageSharingMode(VK10.VK_SHARING_MODE_EXCLUSIVE)
                    .queueFamilyIndexCount(0)
                    .pQueueFamilyIndices(null);
        }

        // Recreation of a swapchain is expensive if available provide old swapchain to speedup recreation
        if (oldSwapchain != null) {
            createInfo.oldSwapchain(oldSwapchain.getHandle());
        } else {
            createInfo.oldSwapchain(0L);
        }

        LongBuffer pSwapChain = stack.mallocLong(1);
        Check.vkCheck(KHRSwapchain.vkCreateSwapchainKHR(device.getHandle(), createInfo, null, pSwapChain), "Failed to create swapchain");
        handle = pSwapChain.get(0);

        fencesInFlight = new HashMap<>(imageCount);
    }

    private static VkExtent2D chooseExtent(MemoryStack stack, Window window, VkSurfaceCapabilitiesKHR capabilities) {
        if (capabilities.currentExtent().width() != 0xFFFFFFFF) {
            return capabilities.currentExtent();
        }

        VkExtent2D actualExtent = VkExtent2D.malloc(stack).set(window.getWidth(), window.getHeight());

        VkExtent2D minExtent = capabilities.minImageExtent();
        VkExtent2D maxExtent = capabilities.maxImageExtent();

        actualExtent.width(MathUtil.clamp(minExtent.width(), maxExtent.width(), actualExtent.width()));
        actualExtent.height(MathUtil.clamp(minExtent.height(), maxExtent.height(), actualExtent.height()));

        return actualExtent;
    }

    /**
     * Destroy swapchain
     *
     * @param device vulkan device
     */
    public void destroy(Device device) {
        KHRSwapchain.vkDestroySwapchainKHR(device.getHandle(), handle, null);
    }

    /**
     * Acquires the next image for rendering from swapchain. Waits until the next image has acquired
     *
     * @param stack  memory stack
     * @param device vulkan device
     * @param imageAvailableSemaphore semaphore for waiting for an available image
     * @param imageFence fence to wait for an image
     */
    public int acquireNextImage(MemoryStack stack, Device device, Semaphore imageAvailableSemaphore, Fence imageFence) {
        // Acquire next image
        IntBuffer pImageIndex = stack.mallocInt(1);
        Check.vkCheck(KHRSwapchain.vkAcquireNextImageKHR(device.getHandle(), handle, ~0L, imageAvailableSemaphore.getHandle(),
                0L, pImageIndex), "Failed to acquire next image from swapchain");
        int imageIndex = pImageIndex.get(0);

        // Wait for image fences in flight
        if (fencesInFlight.containsKey(imageIndex)) {
            Check.vkCheck(VK10.vkWaitForFences(device.getHandle(), fencesInFlight.get(imageIndex).getHandle(), true, ~0L),
                    "Failed to wait for image in flight fence");
        }
        fencesInFlight.put(imageIndex, imageFence);

        return imageIndex;
    }

    /**
     * Gathers all swapchain images as image view list
     *
     * @param stack   memory stack
     * @param device  device
     * @return swapchain images as image view list
     */
    public List<Framebuffer> getFramebuffers(MemoryStack stack, Device device, Renderpass renderpass) {
        IntBuffer pImageCount = stack.mallocInt(1);
        Check.vkCheck(KHRSwapchain.vkGetSwapchainImagesKHR(device.getHandle(), handle, pImageCount, null), "Failed to get swapchain image count");
        int imageCount = pImageCount.get(0);

        LongBuffer pImages = stack.mallocLong(imageCount);
        Check.vkCheck(KHRSwapchain.vkGetSwapchainImagesKHR(device.getHandle(), handle, pImageCount, pImages), "Failed to get swapchain images");

        List<Framebuffer> framebuffers = new ArrayList<>(imageCount);
        for (int i = 0; i < imageCount; i++) {
            Image image = new Image(pImages.get(i), format);
            ImageView imageView = new ImageView(stack, device, image);
            Framebuffer framebuffer = new Framebuffer(stack, device, this, renderpass, imageView);
            framebuffers.add(framebuffer);
        }

        return framebuffers;
    }

    /**
     * Gets internal swapchain vulkan handle
     *
     * @return internal swapchain vulkan handle
     */
    public long getHandle() {
        return handle;
    }

    /**
     * Gets swapchain image count
     *
     * @return count of images the swapchain uses
     */
    public int getImageCount() {
        return imageCount;
    }

    /**
     * Gets current swapchain image extent (size)
     *
     * @return swapchain image extent
     */
    public VkExtent2D getExtent() {
        return extent;
    }

    /**
     * Gets swapchain image format
     *
     * @return swapchain format
     */
    public int getFormat() {
        return format;
    }
}
