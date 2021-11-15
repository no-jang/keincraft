package client.render.vk.pipeline;

import client.render.vk.device.Device;
import client.render.vk.pipeline.fixed.ColorBlend;
import client.render.vk.pipeline.fixed.Multisampling;
import client.render.vk.pipeline.fixed.Rasterizer;
import client.render.vk.pipeline.fixed.VertexInput;
import client.render.vk.pipeline.shader.Shader;
import client.render.vk.present.Swapchain;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.LongBuffer;
import java.util.List;

import static client.render.vk.Global.vkCheck;
import static org.lwjgl.vulkan.VK10.*;

// MAYBE Create builder class
public class Pipeline {
    private final long layoutHandle;
    private final long handle;

    public Pipeline(MemoryStack stack, Device device, Swapchain swapchain, Renderpass renderpass, List<Shader> shaders,
                    VertexInput vertexInput, Rasterizer rasterizer, Multisampling multisampling, ColorBlend colorBlend) {
        VkPipelineLayoutCreateInfo layoutCreateInfo = VkPipelineLayoutCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .pSetLayouts(null)
                .pPushConstantRanges(null);

        LongBuffer pPipelineLayout = stack.mallocLong(1);
        vkCheck(vkCreatePipelineLayout(device.getHandle(), layoutCreateInfo, null, pPipelineLayout), "Failed to create pipeline layout");
        layoutHandle = pPipelineLayout.get(0);

        VkPipelineShaderStageCreateInfo.Buffer pShaders = VkPipelineShaderStageCreateInfo.calloc(shaders.size(), stack);

        for (int i = 0; i < shaders.size(); i++) {
            pShaders.put(i, shaders.get(i).getShaderStageCreateInfo());
        }

        VkViewport viewport = VkViewport.malloc(stack)
                .x(0.0f)
                .y(0.0f)
                .width(swapchain.getExtent().width())
                .height(swapchain.getExtent().height())
                .minDepth(0.0f)
                .maxDepth(1.0f);

        VkRect2D scissors = VkRect2D.malloc(stack)
                .extent(swapchain.getExtent())
                .offset(VkOffset2D.malloc(stack)
                        .set(0, 0));

        VkPipelineViewportStateCreateInfo viewportState = VkPipelineViewportStateCreateInfo.calloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .viewportCount(1)
                .pViewports(VkViewport.malloc(1, stack)
                        .put(0, viewport))
                .scissorCount(1)
                .pScissors(VkRect2D.malloc(1, stack)
                        .put(0, scissors));

        VkGraphicsPipelineCreateInfo createInfo = VkGraphicsPipelineCreateInfo.calloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .pStages(pShaders)
                .pVertexInputState(vertexInput.getVertexInput())
                .pInputAssemblyState(vertexInput.getInputAssembly())
                .pViewportState(viewportState)
                .pRasterizationState(rasterizer.getRasterizer())
                .pMultisampleState(multisampling.getMultisampling())
                .pDepthStencilState(null)
                .pColorBlendState(colorBlend.getColorBlend())
                .pDynamicState(null)
                .layout(layoutHandle)
                .renderPass(renderpass.getHandle())
                .subpass(0)
                .basePipelineHandle(VK_NULL_HANDLE)
                .basePipelineIndex(-1);

        LongBuffer pPipeline = stack.mallocLong(1);
        vkCheck(vkCreateGraphicsPipelines(device.getHandle(), VK_NULL_HANDLE, VkGraphicsPipelineCreateInfo.malloc(1, stack).put(0, createInfo),
                null, pPipeline), "Failed to create graphics pipeline");
        handle = pPipeline.get(0);
    }

    public void destroy(Device device) {
        vkDestroyPipeline(device.getHandle(), handle, null);
        vkDestroyPipelineLayout(device.getHandle(), layoutHandle, null);
    }

    public long getHandle() {
        return handle;
    }
}
