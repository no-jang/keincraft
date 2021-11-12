package client.render.vk.device.queue;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkDeviceQueueCreateInfo;

public class Queue {
    private final VkDeviceQueueCreateInfo createInfo;

    public Queue(QueueFamilies families) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            createInfo = VkDeviceQueueCreateInfo.malloc(stack)
                    .sType$Default()
                    .queueFamilyIndex(families.getGraphicsFamilyIndex())
                    .pQueuePriorities(stack.floats(1.0f));
        }
    }

    public VkDeviceQueueCreateInfo getCreateInfo() {
        return createInfo;
    }
}
