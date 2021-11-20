package client.graphics.sync;

import client.graphics.device.Device;
import client.graphics.util.Check;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkSemaphoreCreateInfo;

import java.nio.LongBuffer;

/**
 * Synchronization object with the states signaled, unsignaled and an increasing integer.
 */
public class Semaphore {
    private final long handle;

    /**
     * Creates new semaphore
     *
     * @param stack  memory stack
     * @param device vulkan device
     */
    public Semaphore(MemoryStack stack, Device device) {
        VkSemaphoreCreateInfo createInfo = VkSemaphoreCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0);

        LongBuffer pSemaphore = stack.mallocLong(1);
        Check.vkCheck(VK10.vkCreateSemaphore(device.getHandle(), createInfo, null, pSemaphore), "Failed to create semaphore");
        handle = pSemaphore.get(0);
    }

    /**
     * Destroys semaphore
     *
     * @param device vulkan device
     */
    public void destroy(Device device) {
        VK10.vkDestroySemaphore(device.getHandle(), handle, null);
    }

    /**
     * @return internal vulkan semaphore handle
     */
    public long getHandle() {
        return handle;
    }
}
