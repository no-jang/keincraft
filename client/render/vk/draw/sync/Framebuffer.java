package client.render.vk.draw.sync;

import client.render.vk.Global;
import client.render.vk.device.Device;
import client.render.vk.pipeline.RenderPass;
import client.render.vk.present.SwapChain;
import client.render.vk.present.image.ImageView;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkFramebufferCreateInfo;

import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;

public class Framebuffer {
    private final long handle;

    public Framebuffer(MemoryStack stack, Device device, RenderPass renderpass, SwapChain swapchain, ImageView imageView) {
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

        LongBuffer pFramebuffer = stack.mallocLong(1);
        Global.vkCheck(VK10.vkCreateFramebuffer(device.getHandle(), createInfo, null, pFramebuffer), "Failed to create framebuffer");
        handle = pFramebuffer.get(0);
    }

    public static List<Framebuffer> createFramebuffers(MemoryStack stack, Device device, RenderPass renderpass, SwapChain swapchain, List<ImageView> imageViews) {
        List<Framebuffer> framebuffers = new ArrayList<>(imageViews.size());
        for (ImageView imageView : imageViews) {
            Framebuffer framebuffer = new Framebuffer(stack, device, renderpass, swapchain, imageView);
            framebuffers.add(framebuffer);
        }
        return framebuffers;
    }

    public void destroy(Device device) {
        VK10.vkDestroyFramebuffer(device.getHandle(), handle, null);
    }

    public long getHandle() {
        return handle;
    }
}
