package client.graphics.vk.renderpass;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkAttachmentDescription;
import org.lwjgl.vulkan.VkAttachmentReference;

public class DepthAttachment {
    private final int binding;
    private final VkAttachmentDescription description;
    private final VkAttachmentReference reference;

    public DepthAttachment(MemoryStack stack, int format, int binding) {
        this.binding = binding;

        description = VkAttachmentDescription.malloc(stack)
                .flags(0)
                .loadOp(VK10.VK_ATTACHMENT_LOAD_OP_CLEAR)
                .storeOp(VK10.VK_ATTACHMENT_STORE_OP_STORE)
                .stencilLoadOp(VK10.VK_ATTACHMENT_LOAD_OP_DONT_CARE)
                .stencilStoreOp(VK10.VK_ATTACHMENT_STORE_OP_DONT_CARE)
                .initialLayout(VK10.VK_IMAGE_LAYOUT_UNDEFINED)
                .format(format);

        description.finalLayout(VK10.VK_IMAGE_LAYOUT_DEPTH_STENCIL_ATTACHMENT_OPTIMAL);

        reference = VkAttachmentReference.malloc(stack)
                .attachment(binding)
                .layout(VK10.VK_IMAGE_LAYOUT_COLOR_ATTACHMENT_OPTIMAL);
    }

    public int getBinding() {
        return binding;
    }

    public VkAttachmentReference getReference() {
        return reference;
    }

    public VkAttachmentDescription getDescription() {
        return description;
    }
}
