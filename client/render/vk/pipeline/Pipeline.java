package client.render.vk.pipeline;

import client.render.vk.present.Swapchain;
import client.render.vk.setup.Device;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.LongBuffer;
import java.util.List;

import static client.render.vk.Global.vkCheck;
import static org.lwjgl.vulkan.VK10.*;

public class Pipeline {
    private final long layoutHandle;
    private final long handle;

    public Pipeline(MemoryStack stack, Device device, Swapchain swapchain, Renderpass renderpass, List<Shader> shaders) {
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

            VkPipelineVertexInputStateCreateInfo vertexInput = VkPipelineVertexInputStateCreateInfo.calloc(stack)
                    .sType$Default()
                    .flags(0)
                    .pNext(0)
                    .pVertexBindingDescriptions(null)
                    .pVertexAttributeDescriptions(null);

            VkPipelineInputAssemblyStateCreateInfo inputAssembly = VkPipelineInputAssemblyStateCreateInfo.calloc(stack)
                    .sType$Default()
                    .flags(0)
                    .pNext(0)
                    .topology(VK10.VK_PRIMITIVE_TOPOLOGY_TRIANGLE_LIST)
                    .primitiveRestartEnable(false);

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

            VkPipelineRasterizationStateCreateInfo rasterizer = VkPipelineRasterizationStateCreateInfo.calloc(stack)
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

            VkPipelineColorBlendAttachmentState colorBlendAttachment = VkPipelineColorBlendAttachmentState.malloc(stack)
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
                    .pAttachments(VkPipelineColorBlendAttachmentState.malloc(1, stack)
                            .put(0, colorBlendAttachment))
                    .blendConstants(stack.floats(0.0f, 0.0f, 0.0f, 0.0f));

            VkGraphicsPipelineCreateInfo createInfo = VkGraphicsPipelineCreateInfo.calloc(stack)
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
