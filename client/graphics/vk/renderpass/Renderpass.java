package client.graphics.vk.renderpass;

import client.graphics.vk.device.Device;
import client.graphics.vk.util.Check;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.LongBuffer;
import java.util.List;

public class Renderpass {
    private final long handle;

    public Renderpass(MemoryStack stack, Device device, List<Subpass> subpasses, List<Attachment> attachments) {
        VkSubpassDependency.Buffer pDependencies = VkSubpassDependency.malloc(subpasses.size(), stack);
        VkSubpassDescription.Buffer pSubpasses = VkSubpassDescription.malloc(subpasses.size(), stack);
        VkAttachmentDescription.Buffer pAttachments = VkAttachmentDescription.malloc(attachments.size(), stack);

        VkRenderPassCreateInfo createInfo = VkRenderPassCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .pDependencies(pDependencies)
                .pSubpasses(pSubpasses);

        for (int i = 0; i < subpasses.size(); i++) {
            pDependencies.put(i, subpasses.get(i).getDependency());
            pSubpasses.put(i, subpasses.get(i).getDescription());
        }

        for (int i = 0; i < attachments.size(); i++) {
            pAttachments.put(i, attachments.get(i).getDescription());
        }

        LongBuffer pRenderpass = stack.mallocLong(1);
        Check.vkCheck(VK10.vkCreateRenderPass(device.getHandle(), createInfo, null, pRenderpass), "Failed to create render pass");
        handle = pRenderpass.get(0);
    }

    public void destroy(Device device) {
        VK10.vkDestroyRenderPass(device.getHandle(), handle, null);
    }

    public long getHandle() {
        return handle;
    }
}
