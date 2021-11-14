package client.render.vk.draw;

import client.render.vk.Global;
import client.render.vk.device.Device;
import client.render.vk.device.PhysicalDevice;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkCommandPoolCreateInfo;

import java.nio.LongBuffer;

public class CommandPool {
    private final long handle;

    public CommandPool(MemoryStack stack, PhysicalDevice physicalDevice, Device device) {
        VkCommandPoolCreateInfo createInfo = VkCommandPoolCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .queueFamilyIndex(physicalDevice.getQueueFamilies().getGraphicsFamilyIndex());

        LongBuffer pCommandPool = stack.mallocLong(1);
        Global.vkCheck(VK10.vkCreateCommandPool(device.getHandle(), createInfo, null, pCommandPool), "Failed to create command pool");
        handle = pCommandPool.get(0);
    }

    public void destroy(Device device) {
        VK10.vkDestroyCommandPool(device.getHandle(), handle, null);
    }

    public long getHandle() {
        return handle;
    }
}
