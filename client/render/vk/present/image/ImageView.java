package client.render.vk.present.image;

import client.render.vk.device.Device;
import client.render.vk.present.Swapchain;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkImageViewCreateInfo;

import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;

import static client.render.vk.Global.vkCheck;
import static org.lwjgl.vulkan.VK10.*;

public class ImageView {
    private final long handle;

    public ImageView(MemoryStack stack, Device device, Swapchain swapchain, Image image) {
        VkImageViewCreateInfo createInfo = VkImageViewCreateInfo.malloc(stack)
                .sType$Default()
                .image(image.getHandle())
                .viewType(VK_IMAGE_VIEW_TYPE_2D)
                .format(swapchain.getFormat().format())
                .flags(0)
                .pNext(VK_NULL_HANDLE);

        createInfo.components().r(VK_COMPONENT_SWIZZLE_IDENTITY);
        createInfo.components().g(VK_COMPONENT_SWIZZLE_IDENTITY);
        createInfo.components().b(VK_COMPONENT_SWIZZLE_IDENTITY);
        createInfo.components().a(VK_COMPONENT_SWIZZLE_IDENTITY);

        createInfo.subresourceRange().aspectMask(VK_IMAGE_ASPECT_COLOR_BIT);
        createInfo.subresourceRange().baseMipLevel(0);
        createInfo.subresourceRange().levelCount(1);
        createInfo.subresourceRange().baseArrayLayer(0);
        createInfo.subresourceRange().layerCount(1);

        LongBuffer pHandle = stack.mallocLong(1);
        vkCheck(vkCreateImageView(device.getHandle(), createInfo, null, pHandle),
                "Failed to create image view");
        handle = pHandle.get(0);
    }

    public static List<ImageView> createImageViews(MemoryStack stack, Device device, Swapchain swapchain, List<Image> images) {
        List<ImageView> views = new ArrayList<>(images.size());
        for (Image image : images) {
            ImageView view = new ImageView(stack, device, swapchain, image);
            views.add(view);
        }
        return views;
    }

    public void destroy(Device device) {
        vkDestroyImageView(device.getHandle(), handle, null);
    }

    public long getHandle() {
        return handle;
    }
}
