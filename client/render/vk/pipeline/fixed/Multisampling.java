package client.render.vk.pipeline.fixed;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkPipelineMultisampleStateCreateInfo;

public class Multisampling {
    private final VkPipelineMultisampleStateCreateInfo multisampling;

    public Multisampling(MemoryStack stack) {
        multisampling = VkPipelineMultisampleStateCreateInfo.calloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .sampleShadingEnable(false)
                .rasterizationSamples(VK10.VK_SAMPLE_COUNT_1_BIT)
                .minSampleShading(1.0f)
                .pSampleMask(null)
                .alphaToCoverageEnable(false)
                .alphaToOneEnable(false);
    }

    public VkPipelineMultisampleStateCreateInfo getMultisampling() {
        return multisampling;
    }
}
