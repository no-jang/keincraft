package client.render.vk.device;

import client.render.vk.device.queue.QueueFamily;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkDeviceCreateInfo;
import org.lwjgl.vulkan.VkDeviceQueueCreateInfo;

import java.util.List;

import static client.render.vk.Global.vkCheck;
import static org.lwjgl.vulkan.VK10.*;

public class Device {
    private final VkDevice handle;

    public Device(MemoryStack stack, PhysicalDevice physicalDevice, List<QueueFamily> queues) {
        VkDeviceQueueCreateInfo.Buffer pQueueCreateInfos = VkDeviceQueueCreateInfo.calloc(queues.size(), stack)
                .flags(0);

        VkDeviceCreateInfo createInfo = VkDeviceCreateInfo.malloc(stack)
                .sType$Default()
                .pQueueCreateInfos(pQueueCreateInfos)
                .pEnabledFeatures(physicalDevice.getRequiredFeatures())
                .ppEnabledExtensionNames(physicalDevice.getRequiredExtensions())
                .ppEnabledLayerNames(null);

        for (QueueFamily queue : queues) {
            pQueueCreateInfos.put(queue.getCreateInfo());
        }

        PointerBuffer pDevice = stack.mallocPointer(1);
        vkCheck(vkCreateDevice(physicalDevice.getHandle(), createInfo, null, pDevice), "Failed to create logical device");
        handle = new VkDevice(pDevice.get(0), physicalDevice.getHandle(), createInfo);
    }

    public void destroy() {
        vkDestroyDevice(handle, null);
    }

    public void waitIdle() {
        vkDeviceWaitIdle(handle);
    }

    public VkDevice getHandle() {
        return handle;
    }
}
