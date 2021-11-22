package client.graphics2.vk.command;

import client.graphics2.vk.device.Device;
import client.graphics2.vk.pipeline.Pipeline;
import client.graphics2.vk.renderpass.Framebuffers;
import client.graphics2.vk.renderpass.Renderpass;
import client.graphics2.vk.renderpass.Swapchain;
import client.render.vk.Global;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

public class CommandBuffer {
    private final VkCommandBuffer handle;

    public CommandBuffer(Device device, long pCommandBuffer) {
        this.handle = new VkCommandBuffer(pCommandBuffer, device.getHandle());
    }

    public void begin(MemoryStack stack, Swapchain swapchain) {
        VkCommandBufferBeginInfo beginInfo = VkCommandBufferBeginInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .pInheritanceInfo(null);

        Global.vkCheck(VK10.vkBeginCommandBuffer(handle, beginInfo), "Failed to begin recording command buffer");

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

        VK10.vkCmdSetViewport(handle, 0, VkViewport.malloc(1, stack).put(0, viewport));
        VK10.vkCmdSetScissor(handle, 0, VkRect2D.malloc(1, stack).put(0, scissors));
    }

    public void beginRenderPass(MemoryStack stack, Renderpass renderpass, Swapchain swapchain, Framebuffers framebuffers) {
        long framebuffer;
        if (swapchain.getActiveImageIndex() > framebuffers.getFramebuffers().size()) {
            framebuffer = framebuffers.getFramebuffers().get(0);
        } else {
            framebuffer = framebuffers.getFramebuffers().get(swapchain.getActiveImageIndex());
        }

        VkRenderPassBeginInfo renderPassBeginInfo = VkRenderPassBeginInfo.malloc(stack)
                .sType$Default()
                .pNext(0)
                .renderPass(renderpass.getHandle())
                .framebuffer(framebuffer)
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

        VK10.vkCmdBeginRenderPass(handle, renderPassBeginInfo, VK10.VK_SUBPASS_CONTENTS_INLINE);
    }

    public void beginPipeline(Pipeline pipeline) {
        VK10.vkCmdBindPipeline(handle, VK10.VK_PIPELINE_BIND_POINT_GRAPHICS, pipeline.getHandle());
    }

    public void draw(int vertexCount, int instanceCount, int firstVertex, int firstInstance) {
        VK10.vkCmdDraw(handle, vertexCount, instanceCount, firstVertex, firstInstance);
    }

    public void endRenderPass() {
        VK10.vkCmdEndRenderPass(handle);
    }

    public void end() {
        Global.vkCheck(VK10.vkEndCommandBuffer(handle), "Failed to end command buffer");
    }

    public VkCommandBuffer getHandle() {
        return handle;
    }
}
