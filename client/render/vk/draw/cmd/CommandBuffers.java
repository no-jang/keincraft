package client.render.vk.draw.cmd;

import client.graphics.vk.device.Device;
import client.graphics.vk.renderpass.Framebuffers;
import client.render.vk.Global;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkCommandBufferAllocateInfo;

import java.util.ArrayList;
import java.util.List;

public class CommandBuffers {
    private final List<CommandBuffer> buffers;

    public CommandBuffers(MemoryStack stack, Device device, CommandPool commandPool, Framebuffers framebuffers) {
        buffers = new ArrayList<>(framebuffers.getFramebuffers().size());

        VkCommandBufferAllocateInfo allocateInfo = VkCommandBufferAllocateInfo.malloc(stack)
                .sType$Default()
                .pNext(0)
                .commandPool(commandPool.getHandle())
                .level(VK10.VK_COMMAND_BUFFER_LEVEL_PRIMARY)
                .commandBufferCount(framebuffers.getFramebuffers().size());

        PointerBuffer pCommandBuffers = stack.mallocPointer(framebuffers.getFramebuffers().size());
        Global.vkCheck(VK10.vkAllocateCommandBuffers(device.getHandle(), allocateInfo, pCommandBuffers), "Failed to allocate command buffers");

        for (int i = 0; i < framebuffers.getFramebuffers().size(); i++) {
            CommandBuffer commandBuffer = new CommandBuffer(stack, device, framebuffers.getFramebuffers().get(i), pCommandBuffers.get(i));
            buffers.add(commandBuffer);
        }
    }

    public void record(CommandRecording recording) {
        for (CommandBuffer commandBuffer : buffers) {
            commandBuffer.begin();
            recording.record(commandBuffer);
            commandBuffer.end();
        }
    }

    public void update(Framebuffers framebuffers) {
        if (framebuffers.getFramebuffers().size() != buffers.size()) {
            throw new RuntimeException("Failed to update command buffer framebuffers: there " + framebuffers.getFramebuffers().size() + " framebuffers for " + buffers.size() + " command buffers");
        }

        for (int i = 0; i < framebuffers.getFramebuffers().size(); i++) {
            buffers.get(i).update(framebuffers.getFramebuffers().get(i));
        }
    }

    public void reset() {
        for (CommandBuffer commandBuffer : buffers) {
            commandBuffer.reset();
        }
    }

    public List<CommandBuffer> getBuffers() {
        return buffers;
    }

    public CommandBuffer getBuffer(int index) {
        return buffers.get(index);
    }
}
