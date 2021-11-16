package client.render.vk.draw.cmd;

import client.render.vk.Global;
import client.render.vk.device.Device;
import client.render.vk.draw.sync.Framebuffer;
import client.render.vk.pipeline.Pipeline;
import client.render.vk.pipeline.RenderPass;
import client.render.vk.present.SwapChain;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

public class CommandBuffer {
    private final MemoryStack stack;
    private final Framebuffer framebuffer;
    private final VkCommandBuffer handle;

    public CommandBuffer(MemoryStack stack, Device device, Framebuffer framebuffer, long pCommandBuffer) {
        this.stack = stack;
        this.framebuffer = framebuffer;

        this.handle = new VkCommandBuffer(pCommandBuffer, device.getHandle());
    }

    public void begin() {
        VkCommandBufferBeginInfo beginInfo = VkCommandBufferBeginInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .pInheritanceInfo(null);

        Global.vkCheck(VK10.vkBeginCommandBuffer(handle, beginInfo), "Failed to begin recording command buffer");
    }

    public void beginRenderPass(SwapChain swapchain, RenderPass renderpass) {
        VkRenderPassBeginInfo renderPassBeginInfo = VkRenderPassBeginInfo.malloc(stack)
                .sType$Default()
                .pNext(0)
                .renderPass(renderpass.getHandle())
                .framebuffer(framebuffer.getHandle())
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
