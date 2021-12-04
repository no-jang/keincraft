package client.graphics.vk.renderpass;

import client.graphics.vk.device.Device;
import client.graphics.vk.image.ImageView;
import client.graphics.vk.util.Check;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkFramebufferCreateInfo;

import java.nio.LongBuffer;

public class Framebuffer {
    private final long handle;
    private final ImageView imageView;

    public Framebuffer(MemoryStack stack, Device device, Swapchain swapchain, Renderpass renderpass, ImageView imageView) {
        this.imageView = imageView;

        VkFramebufferCreateInfo createInfo = VkFramebufferCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .renderPass(renderpass.getHandle())
                .attachmentCount(1)
                .pAttachments(stack.longs(imageView.getHandle()))
                .width(swapchain.getExtent().width())
                .height(swapchain.getExtent().height())
                .layers(1);

        LongBuffer pHandle = stack.mallocLong(1);
        Check.vkCheck(VK10.vkCreateFramebuffer(device.getHandle(), createInfo, null, pHandle), "Failed to create framebuffer");
        handle = pHandle.get(0);
    }

    public void destroy(Device device) {
        VK10.vkDestroyFramebuffer(device.getHandle(), handle, null);
    }

    public long getHandle() {
        return handle;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
