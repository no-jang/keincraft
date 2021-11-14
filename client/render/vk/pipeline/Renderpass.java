package client.render.vk.pipeline;

import client.render.vk.Global;
import client.render.vk.present.Swapchain;
import client.render.vk.setup.Device;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.LongBuffer;

public class Renderpass {
    private final long handle;

    public Renderpass(MemoryStack stack, Device device, Swapchain swapchain) {
        VkAttachmentDescription colorAttachment = VkAttachmentDescription.malloc(stack)
                .flags(0)
                .format(swapchain.getFormat().format())
                .samples(VK10.VK_SAMPLE_COUNT_1_BIT)
                .loadOp(VK10.VK_ATTACHMENT_LOAD_OP_CLEAR)
                .storeOp(VK10.VK_ATTACHMENT_STORE_OP_STORE)
                .stencilLoadOp(VK10.VK_ATTACHMENT_LOAD_OP_DONT_CARE)
                .stencilStoreOp(VK10.VK_ATTACHMENT_STORE_OP_DONT_CARE)
                .initialLayout(VK10.VK_IMAGE_LAYOUT_UNDEFINED)
                .finalLayout(KHRSwapchain.VK_IMAGE_LAYOUT_PRESENT_SRC_KHR);

            VkAttachmentReference colorAttachmentReference = VkAttachmentReference.malloc(stack)
                    .attachment(0)
                    .layout(VK10.VK_IMAGE_LAYOUT_COLOR_ATTACHMENT_OPTIMAL);

            VkSubpassDescription subpass = VkSubpassDescription.calloc(stack)
                    .flags(0)
                    .pipelineBindPoint(VK10.VK_PIPELINE_BIND_POINT_GRAPHICS)
                    .colorAttachmentCount(1)
                    .pColorAttachments(VkAttachmentReference.malloc(1, stack)
                            .put(0, colorAttachmentReference));

            VkSubpassDependency dependency = VkSubpassDependency.malloc(stack)
                    .srcSubpass(VK10.VK_SUBPASS_EXTERNAL)
                    .dstSubpass(0)
                    .srcStageMask(VK10.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT)
                    .srcAccessMask(0)
                    .dstStageMask(VK10.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT)
                    .dstAccessMask(VK10.VK_ACCESS_COLOR_ATTACHMENT_WRITE_BIT);

            VkRenderPassCreateInfo createInfo = VkRenderPassCreateInfo.malloc(stack)
                    .sType$Default()
                    .flags(0)
                    .pNext(0)
                    .pDependencies(VkSubpassDependency.malloc(1, stack).put(0, dependency))
                    .pAttachments(VkAttachmentDescription.malloc(1, stack)
                            .put(0, colorAttachment))
                    .pSubpasses(VkSubpassDescription.malloc(1, stack)
                            .put(0, subpass));

            LongBuffer pRenderPass = stack.mallocLong(1);
            Global.vkCheck(VK10.vkCreateRenderPass(device.getHandle(), createInfo, null, pRenderPass), "Failed to create render pass");
            handle = pRenderPass.get(0);
    }

    public void destroy(Device device) {
        VK10.vkDestroyRenderPass(device.getHandle(), handle, null);
    }

    public long getHandle() {
        return handle;
    }
}
