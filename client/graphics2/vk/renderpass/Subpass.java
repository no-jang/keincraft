package client.graphics2.vk.renderpass;

import java.util.List;

// TODO Merge with RenderPass
public class Subpass {
    private final int binding;
    private final List<Attachment> attachments;

    public Subpass(int binding, List<Attachment> attachments) {
        this.binding = binding;
        this.attachments = attachments;
    }

    public int getBinding() {
        return binding;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    /*VkAttachmentReference.Buffer pAttachmentReferences = VkAttachmentReference.malloc(attachments.size(), stack);

        description = VkSubpassDescription.malloc(stack)
                .flags(0)
                .pipelineBindPoint(VK10.VK_PIPELINE_BIND_POINT_GRAPHICS)
                .colorAttachmentCount(attachments.size())
                .pColorAttachments(pAttachmentReferences);

        for (int i = 0; i < attachments.size(); i++) {
            pAttachmentReferences.put(i, attachments.get(i).getReference());
        }

        if (depthAttachment != null) {
            description.pDepthStencilAttachment(depthAttachment.getReference());
        }

        dependency = VkSubpassDependency.malloc(stack)
                .srcStageMask(VK10.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT)
                .dstStageMask(VK10.VK_PIPELINE_STAGE_FRAGMENT_SHADER_BIT)
                .srcAccessMask(VK10.VK_ACCESS_COLOR_ATTACHMENT_WRITE_BIT)
                .dstAccessMask(VK10.VK_ACCESS_SHADER_READ_BIT)
                .dependencyFlags(VK10.VK_DEPENDENCY_BY_REGION_BIT);

        if (lastSubPass) {
            dependency.dstSubpass(VK10.VK_SUBPASS_EXTERNAL);
            dependency.dstStageMask(VK10.VK_PIPELINE_STAGE_BOTTOM_OF_PIPE_BIT);
            dependency.srcAccessMask(VK10.VK_ACCESS_COLOR_ATTACHMENT_READ_BIT | VK10.VK_ACCESS_COLOR_ATTACHMENT_WRITE_BIT);
            dependency.dstAccessMask(VK10.VK_ACCESS_MEMORY_READ_BIT);
        } else {
            dependency.dstSubpass(binding);
        }

        if (binding == 0) {
            dependency.srcSubpass(VK10.VK_SUBPASS_EXTERNAL);
            dependency.srcStageMask(VK10.VK_PIPELINE_STAGE_BOTTOM_OF_PIPE_BIT);
            dependency.dstStageMask(VK10.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT);
            dependency.srcAccessMask(VK10.VK_ACCESS_MEMORY_READ_BIT);
            dependency.dstAccessMask(VK10.VK_ACCESS_COLOR_ATTACHMENT_READ_BIT | VK10.VK_ACCESS_COLOR_ATTACHMENT_WRITE_BIT);
        } else {
            dependency.srcSubpass(binding - 1);
        }
    }

    public VkSubpassDependency getDependency() {
        return dependency;
    }

    public VkSubpassDescription getDescription() {
        return description;
    }*/
}
