package client.render.vk.pipeline.part;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkPipelineColorBlendAttachmentState;
import org.lwjgl.vulkan.VkPipelineColorBlendStateCreateInfo;

import static org.lwjgl.vulkan.VK10.*;

public class ColorBlend {
    private final VkPipelineColorBlendStateCreateInfo colorBlend;

    public ColorBlend(MemoryStack stack) {
        VkPipelineColorBlendAttachmentState colorBlendAttachment = VkPipelineColorBlendAttachmentState.malloc(stack)
                .colorWriteMask(VK10.VK_COLOR_COMPONENT_R_BIT | VK10.VK_COLOR_COMPONENT_G_BIT | VK10.VK_COLOR_COMPONENT_B_BIT | VK10.VK_COLOR_COMPONENT_A_BIT)
                .blendEnable(true)
                .srcColorBlendFactor(VK_BLEND_FACTOR_SRC_ALPHA)
                .dstColorBlendFactor(VK10.VK_BLEND_FACTOR_ONE_MINUS_SRC_ALPHA)
                .colorBlendOp(VK10.VK_BLEND_OP_ADD)
                .srcAlphaBlendFactor(VK_BLEND_FACTOR_ONE)
                .dstAlphaBlendFactor(VK10.VK_BLEND_FACTOR_ZERO)
                .alphaBlendOp(VK10.VK_BLEND_OP_ADD);

        colorBlend = VkPipelineColorBlendStateCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .logicOpEnable(false)
                .logicOp(VK_LOGIC_OP_COPY)
                .pAttachments(VkPipelineColorBlendAttachmentState.malloc(1, stack)
                        .put(0, colorBlendAttachment))
                .blendConstants(stack.floats(0.0f, 0.0f, 0.0f, 0.0f));
    }

    public VkPipelineColorBlendStateCreateInfo getColorBlend() {
        return colorBlend;
    }
}
