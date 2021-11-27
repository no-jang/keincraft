package client.graphics2.vk.renderpass;

import client.graphics2.vk.device.Device;
import client.graphics2.vk.util.Check;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.KHRSwapchain;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkAttachmentDescription;
import org.lwjgl.vulkan.VkAttachmentReference;
import org.lwjgl.vulkan.VkRenderPassCreateInfo;
import org.lwjgl.vulkan.VkSubpassDependency;
import org.lwjgl.vulkan.VkSubpassDescription;

import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;

public class Renderpass {
    private final long handle;

    public Renderpass(MemoryStack stack, Device device, List<Subpass> subpasses, List<Attachment> attachments) {
        VkAttachmentDescription.Buffer pDescriptions = VkAttachmentDescription.malloc(attachments.size(), stack);
        VkSubpassDependency.Buffer pDependencies = VkSubpassDependency.malloc(subpasses.size(), stack);
        VkSubpassDescription.Buffer pSubpasses = VkSubpassDescription.malloc(subpasses.size(), stack);

        VkRenderPassCreateInfo createInfo = VkRenderPassCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .pAttachments(pDescriptions)
                .pDependencies(pDependencies)
                .pSubpasses(pSubpasses);

        mallocAttachmentDescriptions(pDescriptions, attachments);
        mallocDependencies(pDependencies, subpasses);
        mallocSubpasses(stack, pSubpasses, subpasses, attachments);

        LongBuffer pRenderpass = stack.mallocLong(1);
        Check.vkCheck(VK10.vkCreateRenderPass(device.getHandle(), createInfo, null, pRenderpass), "Failed to create render pass");
        handle = pRenderpass.get(0);
    }

    private static void mallocSubpasses(MemoryStack stack, VkSubpassDescription.Buffer pSubpasses, List<Subpass> subpasses, List<Attachment> attachments) {
        for (int i = 0; i < subpasses.size(); i++) {
            Subpass subpass = subpasses.get(i);

            int depthAttachment = -1;
            List<VkAttachmentReference> colorAttachments = new ArrayList<>(subpass.getAttachments().size());

            for (int j = 0; j < subpass.getAttachments().size(); j++) {
                Attachment subpassAttachment = subpass.getAttachments().get(j);

                if (!attachments.contains(subpassAttachment)) {
                    throw new RuntimeException("Failed to find a renderpass attachment bound to: " + subpassAttachment.getBinding());
                }

                if (subpassAttachment.getType() == AttachmentType.Depth) {
                    depthAttachment = subpassAttachment.getBinding();
                    continue;
                }

                VkAttachmentReference colorAttachment = VkAttachmentReference.malloc(stack)
                        .attachment(subpassAttachment.getBinding())
                        .layout(VK10.VK_IMAGE_LAYOUT_COLOR_ATTACHMENT_OPTIMAL);

                colorAttachments.add(colorAttachment);
            }

            VkAttachmentReference.Buffer pColorAttachments = VkAttachmentReference.malloc(attachments.size(), stack);

            VkSubpassDescription description = pSubpasses.get(i)
                    .pipelineBindPoint(VK10.VK_PIPELINE_BIND_POINT_GRAPHICS)
                    .colorAttachmentCount(colorAttachments.size())
                    .pColorAttachments(pColorAttachments);

            for (int j = 0; j < colorAttachments.size(); j++) {
                pColorAttachments.put(j, colorAttachments.get(j));
            }

            if (depthAttachment != -1) {
                description.pDepthStencilAttachment(VkAttachmentReference.malloc(stack)
                        .attachment(depthAttachment)
                        .layout(VK10.VK_IMAGE_LAYOUT_DEPTH_STENCIL_ATTACHMENT_OPTIMAL));
            }
        }
    }

    private static void mallocDependencies(VkSubpassDependency.Buffer pDependencies, List<Subpass> subpasses) {
        for (int i = 0; i < subpasses.size(); i++) {
            Subpass subpass = subpasses.get(i);

            VkSubpassDependency dependency = pDependencies.get(i)
                    .srcStageMask(VK10.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT)
                    .dstStageMask(VK10.VK_PIPELINE_STAGE_FRAGMENT_SHADER_BIT)
                    .srcAccessMask(VK10.VK_ACCESS_COLOR_ATTACHMENT_WRITE_BIT)
                    .dstAccessMask(VK10.VK_ACCESS_SHADER_READ_BIT)
                    .dependencyFlags(VK10.VK_DEPENDENCY_BY_REGION_BIT);

            if (subpass.getBinding() == subpasses.size()) {
                dependency.dstSubpass(VK10.VK_SUBPASS_EXTERNAL)
                        .dstStageMask(VK10.VK_PIPELINE_STAGE_BOTTOM_OF_PIPE_BIT)
                        .srcAccessMask(VK10.VK_ACCESS_COLOR_ATTACHMENT_READ_BIT | VK10.VK_ACCESS_COLOR_ATTACHMENT_WRITE_BIT)
                        .dstAccessMask(VK10.VK_ACCESS_MEMORY_READ_BIT);
            } else {
                dependency.dstSubpass(subpass.getBinding());
            }

            if (subpass.getBinding() == 0) {
                dependency.srcSubpass(VK10.VK_SUBPASS_EXTERNAL)
                        .srcStageMask(VK10.VK_PIPELINE_STAGE_BOTTOM_OF_PIPE_BIT)
                        .dstStageMask(VK10.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT)
                        .srcAccessMask(VK10.VK_ACCESS_MEMORY_READ_BIT)
                        .dstAccessMask(VK10.VK_ACCESS_COLOR_ATTACHMENT_READ_BIT | VK10.VK_ACCESS_COLOR_ATTACHMENT_WRITE_BIT);
            } else {
                dependency.srcSubpass(subpass.getBinding() - 1);
            }
        }
    }

    private static void mallocAttachmentDescriptions(VkAttachmentDescription.Buffer pDescriptions, List<Attachment> attachments) {
        for (int i = 0; i < attachments.size(); i++) {
            Attachment attachment = attachments.get(i);

            VkAttachmentDescription description = pDescriptions.get(i)
                    .samples(VK10.VK_SAMPLE_COUNT_1_BIT)
                    .loadOp(VK10.VK_ATTACHMENT_LOAD_OP_CLEAR)
                    .storeOp(VK10.VK_ATTACHMENT_STORE_OP_STORE)
                    .stencilLoadOp(VK10.VK_ATTACHMENT_LOAD_OP_DONT_CARE)
                    .stencilStoreOp(VK10.VK_ATTACHMENT_STORE_OP_DONT_CARE)
                    .initialLayout(VK10.VK_IMAGE_LAYOUT_UNDEFINED)
                    .format(attachment.getFormat());

            switch (attachment.getType()) {
                case Image:
                    description.finalLayout(VK10.VK_IMAGE_LAYOUT_COLOR_ATTACHMENT_OPTIMAL);
                    break;
                case Depth:
                    description.finalLayout(VK10.VK_IMAGE_LAYOUT_DEPTH_STENCIL_ATTACHMENT_OPTIMAL);
                    break;
                case Swapchain:
                    description.finalLayout(KHRSwapchain.VK_IMAGE_LAYOUT_PRESENT_SRC_KHR);
                    break;
            }
        }
    }

    public void destroy(Device device) {
        VK10.vkDestroyRenderPass(device.getHandle(), handle, null);
    }

    public long getHandle() {
        return handle;
    }
}
