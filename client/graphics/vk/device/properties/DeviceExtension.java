package client.graphics.vk.device.properties;

import client.graphics.vk.memory.MemoryContext;
import client.graphics.vk.models.HasValue;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.KHRSwapchain;
import org.lwjgl.vulkan.VkExtensionProperties;

import java.util.ArrayList;
import java.util.List;

public enum DeviceExtension implements HasValue<String> {
    KHR_SWAPCHAIN(KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME);

    private final String value;

    DeviceExtension(String value) {
        this.value = value;
    }

    public static List<DeviceExtension> fromVulkanExtensions(VkExtensionProperties.Buffer pExtensions) {
        List<DeviceExtension> extensions = new ArrayList<>();

        for (int i = 0; i < pExtensions.capacity(); i++) {
            DeviceExtension extension = HasValue.getByValue(pExtensions.get(i).extensionNameString(), DeviceExtension.class);
            if (extension != null) {
                extensions.add(extension);
            }
        }

        return extensions;
    }

    public static PointerBuffer toVulkanBuffer(List<DeviceExtension> extensions) {
        MemoryStack stack = MemoryContext.getStack();
        PointerBuffer pBuffer = stack.mallocPointer(extensions.size());

        for (int i = 0; i < extensions.size(); i++) {
            pBuffer.put(i, stack.ASCII(extensions.get(i).getValue()));
        }

        return pBuffer;
    }

    @Override
    public String getValue() {
        return value;
    }
}
