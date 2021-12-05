package client.graphics.vk.device;

import client.graphics.vk.device.models.DeviceExtension;
import client.graphics.vk.device.models.DeviceFeature;
import client.graphics.vk.device.models.format.FormatProperties;
import client.graphics.vk.device.models.memory.MemoryHeap;
import client.graphics.vk.device.models.memory.MemoryType;
import client.graphics.vk.device.models.properties.DeviceLimits;
import client.graphics.vk.device.models.properties.DeviceProperties;
import client.graphics.vk.device.models.properties.DeviceSpareProperties;
import client.graphics.vk.image.models.Format;
import client.graphics.vk.memory.MemoryContext;
import client.graphics.vk.models.function.EnumerateFunction;
import client.graphics.vk.models.pointers.ReferencePointer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkExtensionProperties;
import org.lwjgl.vulkan.VkFormatProperties;
import org.lwjgl.vulkan.VkMemoryHeap;
import org.lwjgl.vulkan.VkMemoryType;
import org.lwjgl.vulkan.VkPhysicalDevice;
import org.lwjgl.vulkan.VkPhysicalDeviceFeatures;
import org.lwjgl.vulkan.VkPhysicalDeviceMemoryProperties;
import org.lwjgl.vulkan.VkPhysicalDeviceProperties;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhysicalDevice extends ReferencePointer<VkPhysicalDevice> {
    private final VkPhysicalDevice handle;
    private final DeviceProperties properties;
    private final DeviceLimits limits;
    private final List<DeviceSpareProperties> spareProperties;
    private final List<DeviceFeature> features;
    private final List<DeviceExtension> extensions;
    private final List<MemoryType> memoryTypes;
    private final Map<Format, FormatProperties> formatProperties;

    public PhysicalDevice(VkPhysicalDevice handle) {
        this.handle = handle;

        MemoryStack stack = MemoryContext.getStack();

        VkPhysicalDeviceProperties vkProperties = VkPhysicalDeviceProperties.malloc(stack);
        VK10.vkGetPhysicalDeviceProperties(handle, vkProperties);
        properties = new DeviceProperties(vkProperties);

        limits = new DeviceLimits(vkProperties.limits());
        spareProperties = DeviceSpareProperties.fromVulkanSpareProperties(vkProperties.sparseProperties());

        VkPhysicalDeviceFeatures vkFeatures = VkPhysicalDeviceFeatures.malloc(stack);
        VK10.vkGetPhysicalDeviceFeatures(handle, vkFeatures);
        features = DeviceFeature.fromVulkanFeatures(vkFeatures);

        VkExtensionProperties.Buffer extensionProperties = EnumerateFunction.execute(stack.mallocInt(1),
                (pCount, pBuffer) -> VK10.vkEnumerateDeviceExtensionProperties(handle, (String) null, pCount, pBuffer),
                count -> VkExtensionProperties.malloc(count, stack));
        extensions = DeviceExtension.fromVulkanExtensions(extensionProperties);

        VkPhysicalDeviceMemoryProperties memoryProperties = VkPhysicalDeviceMemoryProperties.malloc(stack);
        VK10.vkGetPhysicalDeviceMemoryProperties(handle, memoryProperties);

        int heapCount = memoryProperties.memoryHeapCount();
        VkMemoryHeap.Buffer pHeaps = memoryProperties.memoryHeaps();
        List<MemoryHeap> heaps = new ArrayList<>(heapCount);
        for (int i = 0; i < heapCount; i++) {
            heaps.add(new MemoryHeap(pHeaps.get(i)));
        }

        int typeCount = memoryProperties.memoryTypeCount();
        VkMemoryType.Buffer pTypes = memoryProperties.memoryTypes();
        memoryTypes = new ArrayList<>(typeCount);
        for (int i = 0; i < typeCount; i++) {
            VkMemoryType vkMemoryType = pTypes.get(i);
            memoryTypes.add(new MemoryType(vkMemoryType, heaps.get(vkMemoryType.heapIndex())));
        }

        formatProperties = new HashMap<>();
    }

    public FormatProperties getFormatProperties(Format format) {
        FormatProperties properties = formatProperties.get(format);
        if (properties == null) {
            MemoryStack stack = MemoryContext.getStack();
            VkFormatProperties vkFormatProperties = VkFormatProperties.malloc(stack);
            VK10.vkGetPhysicalDeviceFormatProperties(handle, format.getValue(), vkFormatProperties);
            properties = new FormatProperties(vkFormatProperties);
            formatProperties.put(format, properties);
        }

        return properties;
    }

    public void printDevice() {
        if (!Logger.isDebugEnabled()) {
            return;
        }

        StringBuilder builder = new StringBuilder();

        builder.append("device -> \n");
        builder.append(properties).append("\n");
        builder.append(limits).append("\n");
        builder.append("spare properties: ").append(spareProperties).append("\n");
        builder.append("features: ").append(spareProperties).append("\n");
        builder.append("extensions: ").append(extensions).append("\n");
        builder.append("memory types: ").append(memoryTypes).append("\n");
        builder.append("format properties: ").append(formatProperties).append("\n");

        Logger.debug(builder);
    }

    @Override
    protected long getInternalHandle() {
        return handle.address();
    }

    @Override
    protected VkPhysicalDevice getInternalReference() {
        return handle;
    }

    public DeviceProperties getProperties() {
        return properties;
    }

    public DeviceLimits getLimits() {
        return limits;
    }

    public List<DeviceSpareProperties> getSpareProperties() {
        return spareProperties;
    }

    public List<MemoryType> getMemoryTypes() {
        return memoryTypes;
    }

    public List<DeviceFeature> getFeatures() {
        return features;
    }

    public List<DeviceExtension> getExtensions() {
        return extensions;
    }
}
