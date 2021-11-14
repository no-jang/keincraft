package client.render.vk.setup.queue;

import client.render.vk.setup.Device;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkDeviceQueueCreateInfo;
import org.lwjgl.vulkan.VkQueue;

import static org.lwjgl.vulkan.VK10.vkGetDeviceQueue;

public class Queue {
    private VkDeviceQueueCreateInfo createInfo;
    private VkQueue handle;

    public Queue(MemoryStack stack, int familyIndex) {
        createInfo = VkDeviceQueueCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .queueFamilyIndex(familyIndex)
                .pQueuePriorities(stack.floats(1.0f));
    }

    public void setup(MemoryStack stack, Device device) {
        PointerBuffer pHandle = stack.mallocPointer(1);
        vkGetDeviceQueue(device.getHandle(), createInfo.queueFamilyIndex(), 0, pHandle);
        handle = new VkQueue(pHandle.get(0), device.getHandle());
        createInfo = null;
    }

    public VkDeviceQueueCreateInfo getCreateInfo() {
        return createInfo;
    }

    public VkQueue getHandle() {
        return handle;
    }
}
