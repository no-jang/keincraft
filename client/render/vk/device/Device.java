package client.render.vk.device;

import client.render.vk.device.queue.Queue;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkDeviceCreateInfo;
import org.lwjgl.vulkan.VkDeviceQueueCreateInfo;

import static client.render.vk.Global.vkCheck;
import static org.lwjgl.vulkan.VK10.vkCreateDevice;
import static org.lwjgl.vulkan.VK10.vkDestroyDevice;

public class Device {
    private final VkDevice handle;

    public Device(PhysicalDevice physicalDevice, Queue queue) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            VkDeviceQueueCreateInfo.Buffer pQueueCreateInfos = VkDeviceQueueCreateInfo.malloc(1, stack)
                    .flags(0);

            VkDeviceCreateInfo createInfo = VkDeviceCreateInfo.malloc(stack)
                    .sType$Default()
                    .pQueueCreateInfos(pQueueCreateInfos)
                    .pEnabledFeatures(physicalDevice.getRequiredFeatures())
                    .ppEnabledExtensionNames(physicalDevice.getRequiredExtensions())
                    .ppEnabledLayerNames(null);

            pQueueCreateInfos.put(queue.getCreateInfo());

            PointerBuffer pDevice = stack.mallocPointer(1);
            vkCheck(vkCreateDevice(physicalDevice.getHandle(), createInfo, null, pDevice), "Failed to create logical device");
            handle = new VkDevice(pDevice.get(0), physicalDevice.getHandle(), createInfo);
        }
    }

    public void destroy() {
        vkDestroyDevice(handle, null);
    }

    public VkDevice getHandle() {
        return handle;
    }
}
