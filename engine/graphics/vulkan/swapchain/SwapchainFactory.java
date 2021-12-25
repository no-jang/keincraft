package engine.graphics.vulkan.swapchain;

import engine.graphics.vulkan.device.Device;
import engine.graphics.vulkan.image.ImageFactory;
import engine.graphics.vulkan.image.ImageView;
import engine.graphics.vulkan.image.properties.ImageViewInfo;
import engine.graphics.vulkan.properties.Extent2D;
import engine.graphics.vulkan.queue.QueueFamily;
import engine.graphics.vulkan.surface.Surface;
import engine.graphics.vulkan.surface.properties.PresentMode;
import engine.graphics.vulkan.surface.properties.SurfaceFormat;
import engine.graphics.vulkan.surface.properties.SurfaceTransform;
import engine.graphics.vulkan.swapchain.properties.SwapchainInfo;
import engine.graphics.vulkan.util.function.VkFunction;
import engine.memory.MemoryContext;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.KHRSurface;
import org.lwjgl.vulkan.KHRSwapchain;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkSwapchainCreateInfoKHR;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;

public class SwapchainFactory {
    public static Swapchain createSwapchain(Device device, Surface surface, SwapchainInfo info) {
        return createSwapchain(device, surface, info, null);
    }

    public static Swapchain createSwapchain(Device device, Surface surface, SwapchainInfo info, @Nullable Swapchain oldSwapchain) {
        MemoryStack stack = MemoryContext.getStack();

        SurfaceFormat format = SwapchainUtil.chooseSurfaceFormat(surface, info);
        Extent2D imageExtent = SwapchainUtil.chooseExtent(surface, info);
        SurfaceTransform transform = SwapchainUtil.chooseTransform(surface, info);
        PresentMode presentMode = SwapchainUtil.choosePresentMode(surface, info);
        int minImageCount = SwapchainUtil.chooseMinImageCount(surface, info);

        VkSwapchainCreateInfoKHR createInfo = VkSwapchainCreateInfoKHR.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .imageArrayLayers(1)
                .clipped(true)
                .surface(surface.getHandle())
                .minImageCount(minImageCount)
                .imageFormat(format.getFormat().getValue())
                .imageColorSpace(format.getColorSpace().getValue())
                .imageExtent(imageExtent.toVk(stack))
                .preTransform(transform.getBit())
                .presentMode(presentMode.getValue())
                .imageUsage(VK10.VK_IMAGE_USAGE_COLOR_ATTACHMENT_BIT)
                .compositeAlpha(KHRSurface.VK_COMPOSITE_ALPHA_OPAQUE_BIT_KHR);

        QueueFamily graphicsFamily = info.getGraphicsFamily();
        QueueFamily presentFamily = info.getPresentFamily();

        if (graphicsFamily != null && presentFamily != null && graphicsFamily.getIndex() == presentFamily.getIndex()) {
            createInfo.imageSharingMode(VK10.VK_SHARING_MODE_CONCURRENT)
                    .queueFamilyIndexCount(2)
                    .pQueueFamilyIndices(stack.ints(graphicsFamily.getIndex(), presentFamily.getIndex()));
        } else {
            createInfo.imageSharingMode(VK10.VK_SHARING_MODE_EXCLUSIVE)
                    .queueFamilyIndexCount(0)
                    .pQueueFamilyIndices(null);
        }

        if (oldSwapchain != null) {
            createInfo.oldSwapchain(oldSwapchain.getHandle());
        } else {
            createInfo.oldSwapchain(0L);
        }

        LongBuffer handleBuffer = stack.mallocLong(1);
        VkFunction.execute(() -> KHRSwapchain.vkCreateSwapchainKHR(device.getReference(), createInfo, null, handleBuffer));
        long handle = handleBuffer.get(0);

        IntBuffer imageCountBuffer = stack.mallocInt(1);
        VkFunction.execute(() -> KHRSwapchain.vkGetSwapchainImagesKHR(device.getReference(), handle, imageCountBuffer, null));
        int imageCount = imageCountBuffer.get(0);

        return new Swapchain(handle, device, format, imageExtent, transform, presentMode, minImageCount, imageCount, graphicsFamily, presentFamily);
    }

    public static List<SwapchainImage> createImages(Device device, Swapchain swapchain) {
        MemoryStack stack = MemoryContext.getStack();

        int imageCount = swapchain.getImageCount();
        List<SwapchainImage> images = new ArrayList<>(imageCount);

        IntBuffer imageCountBuffer = stack.ints(imageCount);
        LongBuffer imagesBuffer = stack.mallocLong(imageCount);
        VkFunction.execute(() -> KHRSwapchain.vkGetSwapchainImagesKHR(device.getReference(), swapchain.getHandle(), imageCountBuffer, imagesBuffer));

        for (int i = 0; i < imageCount; i++) {
            images.add(new SwapchainImage(imagesBuffer.get(i), 1, 1, swapchain.getImageExtent(), swapchain.getFormat().getFormat()));
        }

        return images;
    }

    public static List<ImageView> createImageViews(Device device, List<SwapchainImage> images) {
        ImageViewInfo info = new ImageViewInfo.Builder()
                .build();

        List<ImageView> imageViews = new ArrayList<>(images.size());
        for (SwapchainImage image : images) {
            imageViews.add(ImageFactory.createImageView(device, image, info));
        }

        return imageViews;
    }
}
