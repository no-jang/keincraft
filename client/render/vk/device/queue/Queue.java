package client.render.vk.device.queue;

import client.render.vk.device.Device;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkQueue;

import java.util.List;

import static org.lwjgl.vulkan.VK10.vkGetDeviceQueue;

public class Queue {
    private final VkQueue handle;

    public Queue(MemoryStack stack, Device device, QueueFamily family) {
        PointerBuffer pHandle = stack.mallocPointer(1);
        vkGetDeviceQueue(device.getHandle(), family.getCreateInfo().queueFamilyIndex(), 0, pHandle);
        handle = new VkQueue(pHandle.get(0), device.getHandle());
    }

    public static Queue createQueue(MemoryStack stack, Device device, List<QueueFamily> queueFamilies, int familyIndex) {
        QueueFamily family = queueFamilies.stream()
                .filter(f -> f.getCreateInfo().queueFamilyIndex() == familyIndex)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Failed to find requested queue family in device queue families"));
        return new Queue(stack, device, family);
    }

    public VkQueue getHandle() {
        return handle;
    }
}
