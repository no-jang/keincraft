package client.graphics.vk.renderpass;

import client.graphics.vk.device.Device;
import client.graphics.vk.util.Check;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.KHRSwapchain;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkAttachmentDescription;
import org.lwjgl.vulkan.VkAttachmentReference;
import org.lwjgl.vulkan.VkRenderPassCreateInfo;
import org.lwjgl.vulkan.VkSubpassDependency;
import org.lwjgl.vulkan.VkSubpassDescription;

import java.nio.LongBuffer;

public class Renderpass {
    private final long handle;

    public Renderpass(MemoryStack stack, Device device, Swapchain swapchain) {
        VkAttachmentDescription.Buffer pDescriptions = VkAttachmentDescription.malloc(1, stack);
        VkSubpassDescription.Buffer pSubpasses = VkSubpassDescription.calloc(1, stack);
        VkSubpassDependency.Buffer pDependencies = VkSubpassDependency.malloc(1, stack);

        VkRenderPassCreateInfo createInfo = VkRenderPassCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .pAttachments(pDescriptions)
                .pDependencies(pDependencies)
                .pSubpasses(pSubpasses);

        pDescriptions.get(0)
                .flags(0)
                .format(swapchain.getFormat())
                .samples(VK10.VK_SAMPLE_COUNT_1_BIT)
                .loadOp(VK10.VK_ATTACHMENT_LOAD_OP_CLEAR)
                .storeOp(VK10.VK_ATTACHMENT_STORE_OP_STORE)
                .stencilLoadOp(VK10.VK_ATTACHMENT_LOAD_OP_DONT_CARE)
                .stencilStoreOp(VK10.VK_ATTACHMENT_STORE_OP_DONT_CARE)
                .initialLayout(VK10.VK_IMAGE_LAYOUT_UNDEFINED)
                .finalLayout(KHRSwapchain.VK_IMAGE_LAYOUT_PRESENT_SRC_KHR);

        VkAttachmentReference.Buffer pAttachments = VkAttachmentReference.malloc(1, stack);

        pAttachments.get(0)
                .attachment(0)
                .layout(VK10.VK_IMAGE_LAYOUT_COLOR_ATTACHMENT_OPTIMAL);

        pSubpasses.get(0)
                .flags(0)
                .pipelineBindPoint(VK10.VK_PIPELINE_BIND_POINT_GRAPHICS)
                .colorAttachmentCount(1)
                .pColorAttachments(pAttachments);

        pDependencies.get(0)
                .srcSubpass(VK10.VK_SUBPASS_EXTERNAL)
                .dstSubpass(0)
                .srcStageMask(VK10.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT)
                .srcAccessMask(0)
                .dstStageMask(VK10.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT)
                .dstAccessMask(VK10.VK_ACCESS_COLOR_ATTACHMENT_WRITE_BIT)
                .dependencyFlags(0);

        LongBuffer pHandle = stack.mallocLong(1);
        Check.vkCheck(VK10.vkCreateRenderPass(device.getHandle(), createInfo, null, pHandle), "Failed to create render pass");
        handle = pHandle.get(0);
    }

    public void destroy(Device device) {
        VK10.vkDestroyRenderPass(device.getHandle(), handle, null);
    }
}
