package client.graphics2.vk.renderpass;

import client.graphics2.vk.device.Device;
import client.graphics2.vk.util.Check;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkFramebufferCreateInfo;

import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;

// TODO Depth
public class Framebuffers {
    private final List<Long> framebuffers;

    public Framebuffers(MemoryStack stack, Device device, Renderpass renderpass, Swapchain swapchain, List<Attachment> attachments) {
        framebuffers = new ArrayList<>(swapchain.getImageCount());

        for (Attachment attachment : attachments) {
            switch (attachment.getType()) {
                case Swapchain:
                case Depth:
                case Image:
                    // TODO Get image attachments
                    break;
            }
        }

        for (int i = 0; i < swapchain.getImageCount(); i++) {
            LongBuffer pAttachments = stack.mallocLong(attachments.size());

            VkFramebufferCreateInfo createInfo = VkFramebufferCreateInfo.malloc(stack)
                    .sType$Default()
                    .flags(0)
                    .pNext(0)
                    .renderPass(renderpass.getHandle())
                    .attachmentCount(attachments.size())
                    .pAttachments(pAttachments)
                    .width(swapchain.getExtent().width())
                    .height(swapchain.getExtent().height())
                    .layers(1);

            for (int j = 0; j < attachments.size(); j++) {
                Attachment attachment = attachments.get(j);
                switch (attachment.getType()) {
                    // TODO Set image attachments
                    case Image:
                    case Depth:
                        break;
                    case Swapchain:
                        pAttachments.put(j, swapchain.getImageViews().get(i).getHandle());
                }
            }

            LongBuffer pFramebuffer = stack.mallocLong(1);
            Check.vkCheck(VK10.vkCreateFramebuffer(device.getHandle(), createInfo, null, pFramebuffer), "Failed to create framebuffer");
            framebuffers.add(pFramebuffer.get(0));
        }
    }

    public void destroy(Device device) {
        for (long framebuffer : framebuffers) {
            VK10.vkDestroyFramebuffer(device.getHandle(), framebuffer, null);
        }
    }

    public List<Long> getFramebuffers() {
        return framebuffers;
    }
}
