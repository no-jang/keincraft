package client.render.vk.device.queue;

import client.render.vk.device.Device;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkDeviceQueueCreateInfo;
import org.lwjgl.vulkan.VkQueue;

import static org.lwjgl.vulkan.VK10.vkGetDeviceQueue;

public class Queue {
    private VkDeviceQueueCreateInfo createInfo;
    private VkQueue handle;

    public Queue(int familyIndex) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            createInfo = VkDeviceQueueCreateInfo.malloc(stack)
                    .sType$Default()
                    .queueFamilyIndex(familyIndex)
                    .pQueuePriorities(stack.floats(1.0f));
        }
    }

    public void setup(Device device) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            PointerBuffer pHandle = stack.mallocPointer(1);
            vkGetDeviceQueue(device.getHandle(), createInfo.queueFamilyIndex(), 0, pHandle);
            handle = new VkQueue(pHandle.get(0), device.getHandle());
            createInfo = null;
        }
    }

    public VkDeviceQueueCreateInfo getCreateInfo() {
        return createInfo;
    }

    public VkQueue getHandle() {
        return handle;
    }
}
