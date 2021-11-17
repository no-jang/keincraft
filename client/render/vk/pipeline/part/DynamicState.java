package client.render.vk.pipeline.part;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkPipelineDynamicStateCreateInfo;

public class DynamicState {
    private final VkPipelineDynamicStateCreateInfo createInfo;

    public DynamicState(MemoryStack stack) {
        createInfo = VkPipelineDynamicStateCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .pDynamicStates(stack.ints(VK10.VK_DYNAMIC_STATE_SCISSOR, VK10.VK_DYNAMIC_STATE_VIEWPORT));
    }

    public VkPipelineDynamicStateCreateInfo getDynamicState() {
        return createInfo;
    }
}
