package engine.graphics.vulkan.device.queue;

import engine.graphics.vulkan.device.PhysicalDevice;
import engine.memory.MemoryContext;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkQueueFamilyProperties;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class QueueFactory {
    public QueueContainer.Builder createQueueFamilies(PhysicalDevice physicalDevice) {
        MemoryStack stack = MemoryContext.getStack();

        IntBuffer queueFamilyCountBuffer = stack.mallocInt(1);
        VK10.vkGetPhysicalDeviceQueueFamilyProperties(physicalDevice.getReference(), queueFamilyCountBuffer, null);
        int queueFamilyCount = queueFamilyCountBuffer.get(0);

        VkQueueFamilyProperties.Buffer queueFamilyBuffer = VkQueueFamilyProperties.malloc(queueFamilyCount, stack);
        VK10.vkGetPhysicalDeviceQueueFamilyProperties(physicalDevice.getReference(), queueFamilyCountBuffer, queueFamilyBuffer);

        List<QueueFamily> queueFamilies = new ArrayList<>(queueFamilyCount);
        for (int i = 0; i < queueFamilyCount; i++) {
            queueFamilies.add(new QueueFamily(physicalDevice, i, queueFamilyBuffer.get(i)));
        }

        return new QueueContainer.Builder(queueFamilies);
    }
}
