package client.render.vk.draw.cmd;

import client.render.vk.Global;
import client.render.vk.device.Device;
import client.render.vk.draw.sync.Framebuffer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkCommandBufferAllocateInfo;

import java.util.ArrayList;
import java.util.List;

public class CommandBuffers {
    private final List<CommandBuffer> buffers;

    public CommandBuffers(MemoryStack stack, Device device, CommandPool commandPool, List<Framebuffer> framebuffers) {
        buffers = new ArrayList<>(framebuffers.size());

        VkCommandBufferAllocateInfo allocateInfo = VkCommandBufferAllocateInfo.malloc(stack)
                .sType$Default()
                .pNext(0)
                .commandPool(commandPool.getHandle())
                .level(VK10.VK_COMMAND_BUFFER_LEVEL_PRIMARY)
                .commandBufferCount(framebuffers.size());

        PointerBuffer pCommandBuffers = stack.mallocPointer(framebuffers.size());
        Global.vkCheck(VK10.vkAllocateCommandBuffers(device.getHandle(), allocateInfo, pCommandBuffers), "Failed to allocate command buffers");

        for (int i = 0; i < framebuffers.size(); i++) {
            CommandBuffer commandBuffer = new CommandBuffer(stack, device, framebuffers.get(i), pCommandBuffers.get(i));
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

    public void update(List<Framebuffer> framebuffers) {
        if (framebuffers.size() != buffers.size()) {
            throw new RuntimeException("Failed to update command buffer framebuffers: there " + framebuffers.size() + " framebuffers for " + buffers.size() + " command buffers");
        }

        for (int i = 0; i < framebuffers.size(); i++) {
            buffers.get(i).update(framebuffers.get(i));
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
