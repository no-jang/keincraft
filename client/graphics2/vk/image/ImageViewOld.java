package client.graphics2.vk.image;

import client.graphics2.vk.device.Device;
import client.graphics2.vk.device.Surface;
import client.graphics2.vk.util.Check;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkImageViewCreateInfo;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

/**
 * ImageViews represent how an image is presented
 */
// TODO Replace surface with format for textures
public class ImageViewOld {
    private final long handle;
    private final ImageOld image;

    /**
     * Creates new image view for corresponding image
     *
     * @param stack   memory stack
     * @param device  device
     * @param surface surface
     * @param image   corresponding image
     */
    public ImageViewOld(MemoryStack stack, Device device, Surface surface, ImageOld image) {
        this.image = image;

        VkImageViewCreateInfo createInfo = VkImageViewCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .image(image.getHandle())
                .viewType(VK10.VK_IMAGE_TYPE_2D)
                .format(surface.getFormat().format());

        createInfo.components().r(VK_COMPONENT_SWIZZLE_IDENTITY);
        createInfo.components().g(VK_COMPONENT_SWIZZLE_IDENTITY);
        createInfo.components().b(VK_COMPONENT_SWIZZLE_IDENTITY);
        createInfo.components().a(VK_COMPONENT_SWIZZLE_IDENTITY);

        createInfo.subresourceRange().aspectMask(VK_IMAGE_ASPECT_COLOR_BIT);
        createInfo.subresourceRange().baseMipLevel(0);
        createInfo.subresourceRange().levelCount(1);
        createInfo.subresourceRange().baseArrayLayer(0);
        createInfo.subresourceRange().layerCount(1);

        LongBuffer pImageView = stack.mallocLong(1);
        Check.vkCheck(vkCreateImageView(device.getHandle(), createInfo, null, pImageView), "Failed to create image view");
        handle = pImageView.get(0);
    }

    /**
     * Destroy image view
     *
     * @param device vulkan device
     */
    public void destroy(Device device) {
        vkDestroyImageView(device.getHandle(), handle, null);
    }

    /**
     * @return internal vulkan handle
     */
    public long getHandle() {
        return handle;
    }

    /**
     * @return corresponding image
     */
    public ImageOld getImage() {
        return image;
    }
}
