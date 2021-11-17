package client.render.vk.pipeline.part;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkPipelineInputAssemblyStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineVertexInputStateCreateInfo;

public class VertexInput {
    private final VkPipelineVertexInputStateCreateInfo vertexInput;
    private final VkPipelineInputAssemblyStateCreateInfo inputAssembly;

    public VertexInput(MemoryStack stack) {
        vertexInput = VkPipelineVertexInputStateCreateInfo.calloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .pVertexBindingDescriptions(null)
                .pVertexAttributeDescriptions(null);

        inputAssembly = VkPipelineInputAssemblyStateCreateInfo.calloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .topology(VK10.VK_PRIMITIVE_TOPOLOGY_TRIANGLE_LIST)
                .primitiveRestartEnable(false);
    }

    public VkPipelineVertexInputStateCreateInfo getVertexInput() {
        return vertexInput;
    }

    public VkPipelineInputAssemblyStateCreateInfo getInputAssembly() {
        return inputAssembly;
    }
}
