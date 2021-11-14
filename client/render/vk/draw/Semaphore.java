package client.render.vk.draw;

import client.render.vk.Global;
import client.render.vk.setup.Device;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkSemaphoreCreateInfo;

import java.nio.LongBuffer;

public class Semaphore {
    private final long handle;

    public Semaphore(MemoryStack stack, Device device) {
        VkSemaphoreCreateInfo createInfo = VkSemaphoreCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0);

        LongBuffer pSemaphore = stack.mallocLong(1);
        Global.vkCheck(VK10.vkCreateSemaphore(device.getHandle(), createInfo, null, pSemaphore), "Failed to create semaphore");
        handle = pSemaphore.get(0);
    }

    public void destroy(Device device) {
        VK10.vkDestroySemaphore(device.getHandle(), handle, null);
    }

    public long getHandle() {
        return handle;
    }
}
