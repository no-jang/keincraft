package client.graphics.vk.image;

import client.graphics.vk.device.Device;
import client.graphics2.vk.util.Check;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkImageViewCreateInfo;

import java.nio.LongBuffer;

/**
 * ImageViews represent how an image is read and presented
 */
public class ImageView {
    private final long handle;
    private final Image image;

    /**
     * Creates new image view for corresponding image
     *
     * @param stack   memory stack
     * @param device  device
     * @param image   corresponding image
     */
    public ImageView(MemoryStack stack, Device device, Image image) {
        this.image = image;

        VkImageViewCreateInfo createInfo = VkImageViewCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .image(image.getHandle())
                .viewType(VK10.VK_IMAGE_TYPE_2D)
                .format(image.getFormat());

        createInfo.components().r(VK10.VK_COMPONENT_SWIZZLE_IDENTITY);
        createInfo.components().g(VK10.VK_COMPONENT_SWIZZLE_IDENTITY);
        createInfo.components().b(VK10.VK_COMPONENT_SWIZZLE_IDENTITY);
        createInfo.components().a(VK10.VK_COMPONENT_SWIZZLE_IDENTITY);

        createInfo.subresourceRange().aspectMask(VK10.VK_IMAGE_ASPECT_COLOR_BIT);
        createInfo.subresourceRange().baseMipLevel(0);
        createInfo.subresourceRange().levelCount(1);
        createInfo.subresourceRange().baseArrayLayer(0);
        createInfo.subresourceRange().layerCount(1);

        LongBuffer pImageView = stack.mallocLong(1);
        Check.vkCheck(VK10.vkCreateImageView(device.getHandle(), createInfo, null, pImageView), "Failed to create image view");
        handle = pImageView.get(0);
    }

    /**
     * Destroys image view
     *
     * @param device device
     */
    public void destroy(Device device) {
        VK10.vkDestroyImageView(device.getHandle(), handle, null);
    }

    /**
     * Gets internal vulkan image view handle
     *
     * @return internal vulkan handle
     */
    public long getHandle() {
        return handle;
    }

    /**
     * Gets the image this view is referencing to
     *
     * @return image of the view
     */
    public Image getImage() {
        return image;
    }
}
