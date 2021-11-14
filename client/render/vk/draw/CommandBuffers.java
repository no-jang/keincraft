package client.render.vk.draw;

import client.render.vk.Global;
import client.render.vk.pipeline.Pipeline;
import client.render.vk.pipeline.Renderpass;
import client.render.vk.present.Swapchain;
import client.render.vk.setup.Device;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.util.ArrayList;
import java.util.List;

public class CommandBuffers {
    private final List<VkCommandBuffer> handles;

    public CommandBuffers(MemoryStack stack, Device device, CommandPool commandPool, Renderpass renderpass, Swapchain swapchain, Pipeline pipeline, List<Framebuffer> framebuffers) {
        handles = new ArrayList<>(framebuffers.size());

        VkCommandBufferAllocateInfo allocateInfo = VkCommandBufferAllocateInfo.malloc(stack)
                .sType$Default()
                .pNext(0)
                .commandPool(commandPool.getHandle())
                .level(VK10.VK_COMMAND_BUFFER_LEVEL_PRIMARY)
                .commandBufferCount(framebuffers.size());

        PointerBuffer pCommandBuffers = stack.mallocPointer(framebuffers.size());
            Global.vkCheck(VK10.vkAllocateCommandBuffers(device.getHandle(), allocateInfo, pCommandBuffers), "Failed to allocate command buffers");

            for (int i = 0; i < framebuffers.size(); i++) {
                VkCommandBuffer commandBuffer = new VkCommandBuffer(pCommandBuffers.get(i), device.getHandle());
                handles.add(commandBuffer);

                VkCommandBufferBeginInfo beginInfo = VkCommandBufferBeginInfo.malloc(stack)
                        .sType$Default()
                        .flags(0)
                        .pNext(0)
                        .pInheritanceInfo(null);

                Global.vkCheck(VK10.vkBeginCommandBuffer(commandBuffer, beginInfo), "Failed to begin recording command buffer");

                VkRenderPassBeginInfo renderPassBeginInfo = VkRenderPassBeginInfo.malloc(stack)
                        .sType$Default()
                        .pNext(0)
                        .renderPass(renderpass.getHandle())
                        .framebuffer(framebuffers.get(i).getHandle())
                        .renderArea(vkRect2D -> {
                            vkRect2D.offset(vkOffset2D -> vkOffset2D.set(0, 0));
                            vkRect2D.extent(swapchain.getExtent());
                        })
                        .clearValueCount(1)
                        .pClearValues(VkClearValue.malloc(1, stack).color(VkClearColorValue.malloc(stack)
                                .float32(0, 0.0f)
                                .float32(1, 0.0f)
                                .float32(2, 0.0f)
                                .float32(3, 1.0f)));

                VK10.vkCmdBeginRenderPass(commandBuffer, renderPassBeginInfo, VK10.VK_SUBPASS_CONTENTS_INLINE);
                VK10.vkCmdBindPipeline(commandBuffer, VK10.VK_PIPELINE_BIND_POINT_GRAPHICS, pipeline.getHandle());
                VK10.vkCmdDraw(commandBuffer, 3, 1, 0, 0);
                VK10.vkCmdEndRenderPass(commandBuffer);
                Global.vkCheck(VK10.vkEndCommandBuffer(commandBuffer), "Failed to end command buffer");
            }
        }

    public List<VkCommandBuffer> getHandles() {
        return handles;
    }
}
