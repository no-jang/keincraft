package client.render.vk.pipeline.fixed;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkPipelineRasterizationStateCreateInfo;

public class Rasterizer {
    private final VkPipelineRasterizationStateCreateInfo rasterizer;

    public Rasterizer(MemoryStack stack) {
        rasterizer = VkPipelineRasterizationStateCreateInfo.calloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .depthClampEnable(false)
                .rasterizerDiscardEnable(false)
                .polygonMode(VK10.VK_POLYGON_MODE_FILL)
                .lineWidth(1.0f)
                .cullMode(VK10.VK_CULL_MODE_BACK_BIT)
                .frontFace(VK10.VK_FRONT_FACE_CLOCKWISE)
                .depthBiasEnable(false)
                .depthBiasConstantFactor(0.0f)
                .depthBiasClamp(0.0f)
                .depthBiasSlopeFactor(0.0f);
    }

    public VkPipelineRasterizationStateCreateInfo getRasterizer() {
        return rasterizer;
    }
}
