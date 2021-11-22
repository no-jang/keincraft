package client.graphics2.vk.renderpass;

import client.graphics2.Window;
import client.graphics2.vk.device.Device;
import client.graphics2.vk.device.PresentMode;
import client.graphics2.vk.device.Surface;
import client.graphics2.vk.image.ImageOld;
import client.graphics2.vk.image.ImageViewOld;
import client.graphics2.vk.sync.Frame;
import client.graphics2.vk.util.Check;
import common.util.math.MathUtil;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

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
    private final int imageCount;
    private final List<ImageViewOld> imageViews;
    private final VkExtent2D extent;

    private final Map<Integer, Frame> framesInFlight;

    private int activeImageIndex;

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
        VkSurfaceCapabilitiesKHR capabilities = surface.getCapabilities();
        PresentMode presentMode = surface.getPresentMode();

        extent = chooseExtent(stack, window, capabilities);

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
                .imageFormat(surface.getFormat().format())
                .imageColorSpace(surface.getFormat().colorSpace())
                .imageExtent(extent)
                .imageArrayLayers(1)
                .imageUsage(VK10.VK_IMAGE_USAGE_COLOR_ATTACHMENT_BIT)
                .preTransform(capabilities.currentTransform())
                .compositeAlpha(KHRSurface.VK_COMPOSITE_ALPHA_OPAQUE_BIT_KHR)
                .presentMode(presentMode.getIndex())
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

        // Get swapchain images and image view
        IntBuffer pImageCount = stack.mallocInt(1);
        Check.vkCheck(KHRSwapchain.vkGetSwapchainImagesKHR(device.getHandle(), handle, pImageCount, null), "Failed to get swapchain image count");
        int imageCount = pImageCount.get(0);

        LongBuffer pImages = stack.mallocLong(imageCount);
        Check.vkCheck(KHRSwapchain.vkGetSwapchainImagesKHR(device.getHandle(), handle, pImageCount, pImages), "Failed to get swapchain images");

        imageViews = new ArrayList<>(imageCount);
        for (int i = 0; i < imageCount; i++) {
            ImageViewOld view = new ImageViewOld(stack, device, surface, new ImageOld(pImages.get(i)));
            imageViews.add(view);
        }

        framesInFlight = new HashMap<>(imageCount);
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
        for (ImageViewOld view : imageViews) {
            view.destroy(device);
        }

        KHRSwapchain.vkDestroySwapchainKHR(device.getHandle(), handle, null);
    }

    /**
     * Acquires the next image for rendering from swapchain. Waits until the next image has acquired
     *
     * @param stack  memory stack
     * @param device vulkan device
     * @param frame  current frame
     */
    public void acquireNextImage(MemoryStack stack, Device device, Frame frame) {
        // Acquire image
        IntBuffer pImageIndex = stack.mallocInt(1);
        Check.vkCheck(KHRSwapchain.vkAcquireNextImageKHR(device.getHandle(), handle, ~0L, frame.getImageAvailableSemaphore().getHandle(),
                0L, pImageIndex), "Failed to acquire next image from swapchain");
        activeImageIndex = pImageIndex.get(0);

        // Wait for image fence
        if (framesInFlight.containsKey(activeImageIndex)) {
            Check.vkCheck(VK10.vkWaitForFences(device.getHandle(), framesInFlight.get(activeImageIndex).getInFlightFence().getHandle()
                    , true, ~0L), "Failed to wait for image fence");
        }
        framesInFlight.put(activeImageIndex, frame);
    }

    /**
     * @return internal swapchain vulkan handle
     */
    public long getHandle() {
        return handle;
    }

    /**
     * @return count of images the swapchain uses
     */
    public int getImageCount() {
        return imageCount;
    }

    /**
     * @return image views holding images which are being rendered to
     */
    public List<ImageViewOld> getImageViews() {
        return imageViews;
    }

    /**
     * @return current swapchain extent (size)
     */
    public VkExtent2D getExtent() {
        return extent;
    }

    /**
     * @return image index of last acquired image
     */
    public int getActiveImageIndex() {
        return activeImageIndex;
    }
}
