package client.graphics.vk.cmd;

import client.graphics.vk.device.Device;
import client.graphics.vk.util.Check;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkCommandBufferAllocateInfo;
import org.lwjgl.vulkan.VkCommandPoolCreateInfo;

import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;

public class CommandPool {
    private final long handle;
    private final List<CommandBuffer> buffers;

    public CommandPool(MemoryStack stack, Device device) {
        buffers = new ArrayList<>();

        VkCommandPoolCreateInfo createInfo = VkCommandPoolCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .queueFamilyIndex(device.getGraphicsQueueFamilyIndex());

        LongBuffer pHandle = stack.mallocLong(1);
        Check.vkCheck(VK10.vkCreateCommandPool(device.getHandle(), createInfo, null, pHandle), "Failed to create command pool");
        handle = pHandle.get(0);
    }

    public List<CommandBuffer> requestBuffers(MemoryStack stack, Device device, int count) {
        if (count > buffers.size()) {
            int requestBufferCount = count - buffers.size();
            VkCommandBufferAllocateInfo allocateInfo = VkCommandBufferAllocateInfo.malloc(stack)
                    .sType$Default()
                    .pNext(0)
                    .commandPool(handle)
                    .level(VK10.VK_COMMAND_BUFFER_LEVEL_PRIMARY)
                    .commandBufferCount(requestBufferCount);

            PointerBuffer pBuffers = stack.mallocPointer(requestBufferCount);
            Check.vkCheck(VK10.vkAllocateCommandBuffers(device.getHandle(), allocateInfo, pBuffers), "Failed to allocate command buffers");

            for (int i = 0; i < requestBufferCount; i++) {
                CommandBuffer buffer = new CommandBuffer(device, pBuffers.get(i));
                buffers.add(buffer);
            }
        }

        if (count < buffers.size()) {
            return buffers.subList(0, count);
        }

        return buffers;
    }

    public void destroy(Device device) {
        VK10.vkDestroyCommandPool(device.getHandle(), handle, null);
    }

    public long getHandle() {
        return handle;
    }

    public List<CommandBuffer> getBuffers() {
        return buffers;
    }
}
