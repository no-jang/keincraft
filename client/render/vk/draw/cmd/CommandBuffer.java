package client.render.vk.draw.cmd;

import client.graphics.vk.device.Device;
import client.graphics.vk.renderpass.Renderpass;
import client.graphics.vk.renderpass.Swapchain;
import client.render.vk.Global;
import client.render.vk.draw.sync.Framebuffer;
import client.render.vk.pipeline.Pipeline;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

public class CommandBuffer {
    private final MemoryStack stack;
    private final VkCommandBuffer handle;

    private Framebuffer framebuffer;

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

    public void setViewport(Swapchain swapChain) {
        VkViewport viewport = VkViewport.malloc(stack)
                .x(0.0f)
                .y(0.0f)
                .width(swapChain.getExtent().width())
                .height(swapChain.getExtent().height())
                .minDepth(0.0f)
                .maxDepth(1.0f);

        VkRect2D scissors = VkRect2D.malloc(stack)
                .extent(swapChain.getExtent())
                .offset(VkOffset2D.malloc(stack)
                        .set(0, 0));

        VK10.vkCmdSetViewport(handle, 0, VkViewport.malloc(1, stack).put(0, viewport));
        VK10.vkCmdSetScissor(handle, 0, VkRect2D.malloc(1, stack).put(0, scissors));
    }

    public void beginRenderPass(Swapchain swapchain, Renderpass renderpass) {
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

    public void update(Framebuffer framebuffer) {
        this.framebuffer = framebuffer;
    }

    public void reset() {
        Global.vkCheck(VK10.vkResetCommandBuffer(handle, 0), "Failed to reset command buffer");
    }

    public VkCommandBuffer getHandle() {
        return handle;
    }
}
