package client.render.vk.draw;

import client.render.vk.Global;
import client.render.vk.setup.Device;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkFenceCreateInfo;

import java.nio.LongBuffer;

public class Fence {
    private final long handle;

    public Fence(Device device) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            VkFenceCreateInfo createInfo = VkFenceCreateInfo.malloc(stack)
                    .sType$Default()
                    .flags(VK10.VK_FENCE_CREATE_SIGNALED_BIT)
                    .pNext(0);

            LongBuffer pFence = stack.mallocLong(1);
            Global.vkCheck(VK10.vkCreateFence(device.getHandle(), createInfo, null, pFence), "Failed to create fence");
            handle = pFence.get(0);
        }
    }

    public void destroy(Device device) {
        VK10.vkDestroyFence(device.getHandle(), handle, null);
    }

    public long getHandle() {
        return handle;
    }
}
