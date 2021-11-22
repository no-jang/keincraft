package client.graphics2.vk.renderpass;

public class Attachment {
    private final int binding;
    private final AttachmentType type;
    private final int format;

    public Attachment(int binding, AttachmentType type, int format) {
        this.binding = binding;
        this.type = type;
        this.format = format;

/*        description = VkAttachmentDescription.malloc(stack)
                .flags(0)
                .loadOp(VK10.VK_ATTACHMENT_LOAD_OP_CLEAR)
                .storeOp(VK10.VK_ATTACHMENT_STORE_OP_STORE)
                .stencilLoadOp(VK10.VK_ATTACHMENT_LOAD_OP_DONT_CARE)
                .stencilStoreOp(VK10.VK_ATTACHMENT_STORE_OP_DONT_CARE)
                .initialLayout(VK10.VK_IMAGE_LAYOUT_UNDEFINED)
                .format(format);

        if (usedBySwapChain) {
            description.finalLayout(KHRSwapchain.VK_IMAGE_LAYOUT_PRESENT_SRC_KHR);
        } else {
            description.finalLayout(VK10.VK_IMAGE_LAYOUT_COLOR_ATTACHMENT_OPTIMAL);
        }

        reference = VkAttachmentReference.malloc(stack)
                .attachment(binding)
                .layout(VK10.VK_IMAGE_LAYOUT_COLOR_ATTACHMENT_OPTIMAL);*/
    }

    public int getBinding() {
        return binding;
    }

    public AttachmentType getType() {
        return type;
    }

    public int getFormat() {
        return format;
    }
}
