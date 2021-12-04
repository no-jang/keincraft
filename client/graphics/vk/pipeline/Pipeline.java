package client.graphics.vk.pipeline;

import client.graphics.vk.device.Device;
import client.graphics.vk.renderpass.Renderpass;
import client.graphics.vk.renderpass.Swapchain;
import client.graphics.vk.util.Check;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkGraphicsPipelineCreateInfo;
import org.lwjgl.vulkan.VkOffset2D;
import org.lwjgl.vulkan.VkPipelineColorBlendAttachmentState;
import org.lwjgl.vulkan.VkPipelineColorBlendStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineInputAssemblyStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineLayoutCreateInfo;
import org.lwjgl.vulkan.VkPipelineMultisampleStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineRasterizationStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineShaderStageCreateInfo;
import org.lwjgl.vulkan.VkPipelineVertexInputStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineViewportStateCreateInfo;
import org.lwjgl.vulkan.VkRect2D;
import org.lwjgl.vulkan.VkViewport;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.VK_BLEND_FACTOR_ONE;
import static org.lwjgl.vulkan.VK10.VK_BLEND_FACTOR_SRC_ALPHA;
import static org.lwjgl.vulkan.VK10.VK_LOGIC_OP_COPY;
import static org.lwjgl.vulkan.VK10.VK_NULL_HANDLE;
import static org.lwjgl.vulkan.VK10.vkCreateGraphicsPipelines;
import static org.lwjgl.vulkan.VK10.vkCreatePipelineLayout;
import static org.lwjgl.vulkan.VK10.vkDestroyPipeline;
import static org.lwjgl.vulkan.VK10.vkDestroyPipelineLayout;

public class Pipeline {
    private final long handle;
    private final long layoutHandle;

    public Pipeline(MemoryStack stack, Device device, Swapchain swapchain, Renderpass renderpass, Shader[] shaders) {
        VkPipelineVertexInputStateCreateInfo vertexInput = VkPipelineVertexInputStateCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .pVertexBindingDescriptions(null)
                .pVertexAttributeDescriptions(null);

        VkPipelineInputAssemblyStateCreateInfo inputAssembly = VkPipelineInputAssemblyStateCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .topology(VK10.VK_PRIMITIVE_TOPOLOGY_TRIANGLE_LIST)
                .primitiveRestartEnable(false);

        VkPipelineRasterizationStateCreateInfo rasterizer = VkPipelineRasterizationStateCreateInfo.malloc(stack)
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

        VkPipelineMultisampleStateCreateInfo multisampling = VkPipelineMultisampleStateCreateInfo.calloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .sampleShadingEnable(false)
                .rasterizationSamples(VK10.VK_SAMPLE_COUNT_1_BIT)
                .minSampleShading(1.0f)
                .pSampleMask(null)
                .alphaToCoverageEnable(false)
                .alphaToOneEnable(false);

        VkPipelineColorBlendAttachmentState.Buffer colorBlendAttachments = VkPipelineColorBlendAttachmentState.malloc(1, stack);

        colorBlendAttachments.get(0)
                .colorWriteMask(VK10.VK_COLOR_COMPONENT_R_BIT | VK10.VK_COLOR_COMPONENT_G_BIT | VK10.VK_COLOR_COMPONENT_B_BIT | VK10.VK_COLOR_COMPONENT_A_BIT)
                .blendEnable(true)
                .srcColorBlendFactor(VK_BLEND_FACTOR_SRC_ALPHA)
                .dstColorBlendFactor(VK10.VK_BLEND_FACTOR_ONE_MINUS_SRC_ALPHA)
                .colorBlendOp(VK10.VK_BLEND_OP_ADD)
                .srcAlphaBlendFactor(VK_BLEND_FACTOR_ONE)
                .dstAlphaBlendFactor(VK10.VK_BLEND_FACTOR_ZERO)
                .alphaBlendOp(VK10.VK_BLEND_OP_ADD);

        VkPipelineColorBlendStateCreateInfo colorBlend = VkPipelineColorBlendStateCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .logicOpEnable(false)
                .logicOp(VK_LOGIC_OP_COPY)
                .pAttachments(colorBlendAttachments)
                .blendConstants(stack.floats(0.0f, 0.0f, 0.0f, 0.0f));

        int shaderCount = shaders.length;
        VkPipelineShaderStageCreateInfo.Buffer pShaders = VkPipelineShaderStageCreateInfo.malloc(shaderCount, stack);

        for (int i = 0; i < shaderCount; i++) {
            pShaders.get(i)
                    .sType$Default()
                    .flags(0)
                    .pNext(0)
                    .module(shaders[i].getHandle())
                    .stage(shaders[i].getStage())
                    .pName(stack.UTF8("main"));
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
                .offset(VkOffset2D.malloc(stack).set(0, 0));

        VkPipelineViewportStateCreateInfo viewportState = VkPipelineViewportStateCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .viewportCount(1)
                .pViewports(VkViewport.malloc(1, stack).put(0, viewport))
                .scissorCount(1)
                .pScissors(VkRect2D.malloc(1, stack).put(0, scissors));

        VkPipelineLayoutCreateInfo layoutCreateInfo = VkPipelineLayoutCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .pSetLayouts(null)
                .pPushConstantRanges(null);

        LongBuffer pHandle = stack.mallocLong(1);
        Check.vkCheck(vkCreatePipelineLayout(device.getHandle(), layoutCreateInfo, null, pHandle), "Failed to create pipeline layout");
        layoutHandle = pHandle.get(0);

        VkGraphicsPipelineCreateInfo.Buffer pCreateInfo = VkGraphicsPipelineCreateInfo.malloc(1, stack);
        pCreateInfo.get(0)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .pStages(pShaders)
                .pVertexInputState(vertexInput)
                .pInputAssemblyState(inputAssembly)
                .pViewportState(viewportState)
                .pRasterizationState(rasterizer)
                .pMultisampleState(multisampling)
                .pDepthStencilState(null)
                .pColorBlendState(colorBlend)
                .pDynamicState(null)
                .layout(layoutHandle)
                .renderPass(renderpass.getHandle())
                .subpass(0)
                .basePipelineHandle(VK_NULL_HANDLE)
                .basePipelineIndex(-1);

        Check.vkCheck(vkCreateGraphicsPipelines(device.getHandle(), VK_NULL_HANDLE, pCreateInfo, null, pHandle), "Failed to create pipeline");
        handle = pHandle.get(0);
    }

    public void destroy(Device device) {
        vkDestroyPipeline(device.getHandle(), handle, null);
        vkDestroyPipelineLayout(device.getHandle(), layoutHandle, null);
    }

    public long getHandle() {
        return handle;
    }

    public long getLayoutHandle() {
        return layoutHandle;
    }
}
