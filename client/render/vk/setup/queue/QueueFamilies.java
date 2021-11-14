package client.render.vk.setup.queue;

import client.render.vk.present.Surface;
import client.render.vk.setup.PhysicalDevice;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.KHRSurface;
import org.lwjgl.vulkan.VkQueueFamilyProperties;

import java.nio.IntBuffer;
import java.util.stream.IntStream;

import static client.render.vk.Global.vkCheck;
import static org.lwjgl.vulkan.VK10.VK_QUEUE_GRAPHICS_BIT;
import static org.lwjgl.vulkan.VK10.vkGetPhysicalDeviceQueueFamilyProperties;

public class QueueFamilies {
    private int graphicsFamilyIndex = -1;
    private int presentFamilyIndex = -1;

    public QueueFamilies(MemoryStack stack, PhysicalDevice device, Surface surface) {
        // Get queue properties
        IntBuffer pQueueFamilyCount = stack.mallocInt(1);
        vkGetPhysicalDeviceQueueFamilyProperties(device.getHandle(), pQueueFamilyCount, null);
        int queueFamilyCount = pQueueFamilyCount.get(0);

        VkQueueFamilyProperties.Buffer pQueueFamilies = VkQueueFamilyProperties.malloc(queueFamilyCount, stack);
        vkGetPhysicalDeviceQueueFamilyProperties(device.getHandle(), pQueueFamilyCount, pQueueFamilies);

        // Enumerate all queue and check for graphics and presentation queue
        for (int familyIndex = 0; familyIndex < queueFamilyCount; familyIndex++) {
            VkQueueFamilyProperties queueFamily = pQueueFamilies.get(familyIndex);

            // Check if queue supports graphics
            if ((queueFamily.queueFlags() & VK_QUEUE_GRAPHICS_BIT) == 0) {
                this.graphicsFamilyIndex = familyIndex;
            }

            // Check if queue supports presentation
            IntBuffer pPresentSupported = stack.mallocInt(1);
            vkCheck(KHRSurface.vkGetPhysicalDeviceSurfaceSupportKHR(device.getHandle(), familyIndex, surface.getHandle(), pPresentSupported), "Failed to get device present surface queue support");
            if (pPresentSupported.get(0) != 1) {
                this.presentFamilyIndex = familyIndex;
            }
        }
    }

    public int[] unique() {
        return IntStream.of(graphicsFamilyIndex, presentFamilyIndex).distinct().toArray();
    }

    public boolean isSuitable() {
        return graphicsFamilyIndex != -1 && presentFamilyIndex != -1;
    }

    public int getGraphicsFamilyIndex() {
        return graphicsFamilyIndex;
    }

    public int getPresentFamilyIndex() {
        return presentFamilyIndex;
    }
}
