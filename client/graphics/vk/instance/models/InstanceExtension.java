package client.graphics.vk.instance.models;

import client.graphics.vk.memory.MemoryContext;
import client.graphics.vk.models.HasValue;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.EXTDebugReport;
import org.lwjgl.vulkan.VkExtensionProperties;

import java.util.ArrayList;
import java.util.List;

public enum InstanceExtension implements HasValue<String> {
    DEBUG_REPORT(EXTDebugReport.VK_EXT_DEBUG_REPORT_EXTENSION_NAME);

    private final String value;

    InstanceExtension(String value) {
        this.value = value;
    }

    public static List<InstanceExtension> fromVulkanExtensions(VkExtensionProperties.Buffer pExtensions) {
        List<InstanceExtension> extensions = new ArrayList<>();

        for (int i = 0; i < pExtensions.capacity(); i++) {
            InstanceExtension extension = HasValue.getByValue(pExtensions.get(i).extensionNameString(), InstanceExtension.class);
            if (extension != null) {
                extensions.add(extension);
            }
        }

        return extensions;
    }

    public static PointerBuffer toVulkanBuffer(List<InstanceExtension> extensions) {
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
