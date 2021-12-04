package client.graphics.vk.cmd;

import client.graphics.vk.device.Device;
import client.graphics.vk.pipeline.Pipeline;
import client.graphics.vk.renderpass.Framebuffer;
import client.graphics.vk.renderpass.Renderpass;
import client.graphics.vk.renderpass.Swapchain;
import client.graphics.vk.util.Check;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkClearColorValue;
import org.lwjgl.vulkan.VkClearValue;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkCommandBufferBeginInfo;
import org.lwjgl.vulkan.VkRenderPassBeginInfo;

public class CommandBuffer {
    private final VkCommandBuffer handle;

    public CommandBuffer(Device device, long handle) {
        this.handle = new VkCommandBuffer(handle, device.getHandle());
    }

    public void begin(MemoryStack stack) {
        VkCommandBufferBeginInfo beginInfo = VkCommandBufferBeginInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .pInheritanceInfo(null);

        Check.vkCheck(VK10.vkBeginCommandBuffer(handle, beginInfo), "Failed to begin command buffer");
    }

    public void beginRenderpass(MemoryStack stack, Swapchain swapchain, Renderpass renderpass, Framebuffer framebuffer) {
        VkRenderPassBeginInfo beginInfo = VkRenderPassBeginInfo.malloc(stack)
                .sType$Default()
                .pNext(0)
                .renderPass(renderpass.getHandle())
                .framebuffer(framebuffer.getHandle())
                .renderArea(vkRect2D -> {
                    vkRect2D.offset(vkOffset2D -> vkOffset2D.set(0, 0));
                    vkRect2D.extent(swapchain.getExtent());
                })
                .clearValueCount(1)
                .pClearValues(VkClearValue.malloc(1, stack)
                        .color(VkClearColorValue.malloc(stack)
                                .float32(0, 0.0f)
                                .float32(1, 0.0f)
                                .float32(2, 0.0f)
                                .float32(3, 0.0f)));

        VK10.vkCmdBeginRenderPass(handle, beginInfo, VK10.VK_SUBPASS_CONTENTS_INLINE);
    }

    public void bindPipeline(Pipeline pipeline) {
        VK10.vkCmdBindPipeline(handle, VK10.VK_PIPELINE_BIND_POINT_GRAPHICS, pipeline.getHandle());
    }

    public void draw(int vertexCount, int instanceCount, int firstVertex, int firstInstance) {
        VK10.vkCmdDraw(handle, vertexCount, instanceCount, firstVertex, firstInstance);
    }

    public void endRenderpass() {
        VK10.vkCmdEndRenderPass(handle);
    }

    public void end() {
        Check.vkCheck(VK10.vkEndCommandBuffer(handle), "Failed to end command buffer");
    }

    public VkCommandBuffer getHandle() {
        return handle;
    }
}
