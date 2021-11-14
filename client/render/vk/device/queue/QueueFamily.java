package client.render.vk.device.queue;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkDeviceQueueCreateInfo;

import java.util.ArrayList;
import java.util.List;

public class QueueFamily {
    private final VkDeviceQueueCreateInfo createInfo;

    public QueueFamily(MemoryStack stack, int familyIndex) {
        createInfo = VkDeviceQueueCreateInfo.calloc(stack)
                .sType$Default()
                .flags(0)
                .queueFamilyIndex(familyIndex)
                .pQueuePriorities(stack.floats(1.0f));
    }

    public static List<QueueFamily> createQueueFamilies(MemoryStack stack, QueueFamilies families) {
        int[] queueIndices = families.unique();
        List<QueueFamily> queueFamilies = new ArrayList<>(queueIndices.length);
        for (int queueIndex : queueIndices) {
            queueFamilies.add(new QueueFamily(stack, queueIndex));
        }
        return queueFamilies;
    }

    public VkDeviceQueueCreateInfo getCreateInfo() {
        return createInfo;
    }
}
