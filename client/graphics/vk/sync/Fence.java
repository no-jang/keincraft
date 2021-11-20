package client.graphics.vk.sync;

import client.graphics.vk.device.Device;
import client.graphics.vk.util.Check;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkFenceCreateInfo;

import java.nio.LongBuffer;

/**
 * Synchronization object with two states signaled and unsignaled. Can be used for waiting until they have the signaled state.
 */
public class Fence {
    private final long handle;

    /**
     * Creates new fence
     *
     * @param stack  memory stack
     * @param device vulkan device
     */
    public Fence(MemoryStack stack, Device device) {
        VkFenceCreateInfo createInfo = VkFenceCreateInfo.malloc(stack)
                .sType$Default()
                .flags(VK10.VK_FENCE_CREATE_SIGNALED_BIT)
                .pNext(0);

        LongBuffer pFence = stack.mallocLong(1);
        Check.vkCheck(VK10.vkCreateFence(device.getHandle(), createInfo, null, pFence), "Failed to create fence");
        handle = pFence.get(0);
    }

    /**
     * Destroys the fence
     *
     * @param device vulkan device
     */
    public void destroy(Device device) {
        VK10.vkDestroyFence(device.getHandle(), handle, null);
    }

    /**
     * @return internal vulkan fence handle
     */
    public long getHandle() {
        return handle;
    }
}
