package engine.graphics.vulkan.device;

import engine.collections.list.DefaultImmutableList;
import engine.collections.list.ImmutableList;
import engine.graphics.vulkan.device.memory.MemoryType;
import engine.graphics.vulkan.device.properties.DeviceLimits;
import engine.graphics.vulkan.device.properties.DeviceProperties;
import engine.graphics.vulkan.device.properties.DeviceSpareProperties;
import engine.graphics.vulkan.device.properties.FormatProperties;
import engine.graphics.vulkan.image.properties.Format;
import engine.helper.pointer.ReferencePointer;
import engine.memory.MemoryContext;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkFormatProperties;
import org.lwjgl.vulkan.VkPhysicalDevice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhysicalDevice extends ReferencePointer<VkPhysicalDevice> {
    private final DeviceProperties properties;
    private final DeviceLimits limits;
    private final ImmutableList<DeviceSpareProperties> spareProperties;
    private final ImmutableList<MemoryType> memoryTypes;
    private final Map<Format, FormatProperties> formatProperties;

    public PhysicalDevice(VkPhysicalDevice reference,
                          DeviceProperties properties,
                          DeviceLimits limits,
                          List<DeviceSpareProperties> spareProperties,
                          List<MemoryType> memoryTypes) {
        super(reference);
        this.properties = properties;
        this.limits = limits;
        this.spareProperties = new DefaultImmutableList<>(spareProperties);
        this.memoryTypes = new DefaultImmutableList<>(memoryTypes);

        this.formatProperties = new HashMap<>();
    }

    public FormatProperties getFormatProperty(Format format) {
        FormatProperties properties = formatProperties.get(format);

        if (properties == null) {
            MemoryStack stack = MemoryContext.getStack();

            VkFormatProperties vkFormatProperties = VkFormatProperties.malloc(stack);
            VK10.vkGetPhysicalDeviceFormatProperties(reference, format.getValue(), vkFormatProperties);

            properties = new FormatProperties(vkFormatProperties);
            formatProperties.put(format, properties);
        }

        return properties;
    }

    public DeviceProperties getProperties() {
        return properties;
    }

    public ImmutableList<DeviceSpareProperties> getSpareProperties() {
        return spareProperties;
    }

    public DeviceLimits getLimits() {
        return limits;
    }

    public ImmutableList<MemoryType> getMemoryTypes() {
        return memoryTypes;
    }

    @Override
    public String toString() {
        return "PhysicalDevice[id=" + properties.getId() + ", name=" + properties.getName() + ']';
    }

    public String toPropertyString() {
        return "PhysicalDevice[" +
                "\nproperties=" + properties +
                ",\nspareProperties=" + spareProperties +
                ",\nlimits=" + limits +
                ",\nmemoryTypes=" + memoryTypes +
                ",\nformatProperties=" + formatProperties +
                ']';
    }
}
