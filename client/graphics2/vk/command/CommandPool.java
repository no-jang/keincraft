package client.graphics2.vk.command;

import client.graphics2.vk.device.Device;
import client.graphics2.vk.renderpass.Swapchain;
import client.graphics2.vk.util.Check;
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
    private final List<CommandBuffer> commandBuffers;

    public CommandPool(MemoryStack stack, Device device, Swapchain swapchain) {
        VkCommandPoolCreateInfo createInfo = VkCommandPoolCreateInfo.malloc(stack)
                .sType$Default()
                .flags(VK10.VK_COMMAND_POOL_CREATE_RESET_COMMAND_BUFFER_BIT)
                .pNext(0)
                .queueFamilyIndex(device.getGraphicsQueueFamilyIndex());

        LongBuffer pCommandPool = stack.mallocLong(1);
        Check.vkCheck(VK10.vkCreateCommandPool(device.getHandle(), createInfo, null, pCommandPool), "Failed to create command pool");
        handle = pCommandPool.get(0);

        int commandBufferCount = swapchain.getImageCount();
        commandBuffers = new ArrayList<>(commandBufferCount);
        VkCommandBufferAllocateInfo allocateInfo = VkCommandBufferAllocateInfo.malloc(stack)
                .sType$Default()
                .pNext(0)
                .commandPool(handle)
                .level(VK10.VK_COMMAND_BUFFER_LEVEL_PRIMARY)
                .commandBufferCount(commandBufferCount);

        PointerBuffer pCommandBuffers = stack.mallocPointer(commandBufferCount);
        Check.vkCheck(VK10.vkAllocateCommandBuffers(device.getHandle(), allocateInfo, pCommandBuffers), "Failed to create command buffers");

        for (int i = 0; i < commandBufferCount; i++) {
            CommandBuffer commandBuffer = new CommandBuffer(device, pCommandBuffers.get(i));
            commandBuffers.add(commandBuffer);
        }
    }

    public void destroy(Device device) {
        VK10.vkDestroyCommandPool(device.getHandle(), handle, null);
    }

    public long getHandle() {
        return handle;
    }

    public List<CommandBuffer> getCommandBuffers() {
        return commandBuffers;
    }
}
