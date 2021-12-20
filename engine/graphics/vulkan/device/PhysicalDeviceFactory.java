package engine.graphics.vulkan.device;

import engine.graphics.vulkan.device.memory.MemoryHeap;
import engine.graphics.vulkan.device.memory.MemoryType;
import engine.graphics.vulkan.device.properties.DeviceExtension;
import engine.graphics.vulkan.device.properties.DeviceFeature;
import engine.graphics.vulkan.device.properties.DeviceLimits;
import engine.graphics.vulkan.device.properties.DeviceProperties;
import engine.graphics.vulkan.device.properties.DeviceSpareProperties;
import engine.graphics.vulkan.helper.function.VkFunction;
import engine.graphics.vulkan.instance.Instance;
import engine.memory.MemoryContext;
import engine.util.Buffers;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkExtensionProperties;
import org.lwjgl.vulkan.VkMemoryHeap;
import org.lwjgl.vulkan.VkMemoryType;
import org.lwjgl.vulkan.VkPhysicalDevice;
import org.lwjgl.vulkan.VkPhysicalDeviceFeatures;
import org.lwjgl.vulkan.VkPhysicalDeviceMemoryProperties;
import org.lwjgl.vulkan.VkPhysicalDeviceProperties;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class PhysicalDeviceFactory {
    public List<PhysicalDevice> createPhysicalDevices(Instance instance) {
        MemoryStack stack = MemoryContext.getStack();

        IntBuffer physicalDeviceCountBuffer = stack.mallocInt(1);
        VkFunction.execute(() -> VK10.vkEnumeratePhysicalDevices(instance.getReference(), physicalDeviceCountBuffer, null));
        int physicalDeviceCount = physicalDeviceCountBuffer.get(0);

        PointerBuffer physicalDeviceBuffer = stack.mallocPointer(physicalDeviceCount);
        VkFunction.execute(() -> VK10.vkEnumeratePhysicalDevices(instance.getReference(), physicalDeviceCountBuffer, physicalDeviceBuffer));

        List<PhysicalDevice> physicalDevices = new ArrayList<>(physicalDeviceCount);
        for (int i = 0; i < physicalDeviceCount; i++) {
            physicalDevices.add(createPhysicalDevice(new VkPhysicalDevice(physicalDeviceBuffer.get(i), instance.getReference())));
        }

        return physicalDevices;
    }

    public PhysicalDevice createPhysicalDevice(VkPhysicalDevice device) {
        MemoryStack stack = MemoryContext.getStack();

        VkPhysicalDeviceProperties vkProperties = VkPhysicalDeviceProperties.malloc(stack);
        VK10.vkGetPhysicalDeviceProperties(device, vkProperties);

        DeviceProperties properties = new DeviceProperties(vkProperties);
        DeviceLimits limits = new DeviceLimits(vkProperties.limits());
        List<DeviceSpareProperties> spareProperties = DeviceSpareProperties.ofVk(vkProperties.sparseProperties());

        VkPhysicalDeviceFeatures vkFeatures = VkPhysicalDeviceFeatures.malloc(stack);
        VK10.vkGetPhysicalDeviceFeatures(device, vkFeatures);
        List<DeviceFeature> features = DeviceFeature.ofVk(vkFeatures);

        IntBuffer extensionCountBuffer = stack.mallocInt(1);
        VkFunction.execute(() -> VK10.vkEnumerateDeviceExtensionProperties(device, (String) null, extensionCountBuffer, null));
        int extensionCount = extensionCountBuffer.get(0);

        VkExtensionProperties.Buffer extensionBuffer = VkExtensionProperties.malloc(extensionCount, stack);
        VkFunction.execute(() -> VK10.vkEnumerateDeviceExtensionProperties(device, (String) null, extensionCountBuffer, extensionBuffer));
        List<DeviceExtension> extensions = Buffers.fromStructBuffer(extensionBuffer, DeviceExtension.class, VkExtensionProperties::extensionNameString);

        VkPhysicalDeviceMemoryProperties memoryProperties = VkPhysicalDeviceMemoryProperties.malloc(stack);
        VK10.vkGetPhysicalDeviceMemoryProperties(device, memoryProperties);

        int heapCount = memoryProperties.memoryHeapCount();
        VkMemoryHeap.Buffer heapBuffer = memoryProperties.memoryHeaps();
        List<MemoryHeap> heaps = new ArrayList<>(heapCount);
        for (int i = 0; i < heapCount; i++) {
            heaps.add(new MemoryHeap(heapBuffer.get(i)));
        }

        int typeCount = memoryProperties.memoryTypeCount();
        VkMemoryType.Buffer typeBuffer = memoryProperties.memoryTypes();
        List<MemoryType> types = new ArrayList<>(typeCount);
        for (int i = 0; i < typeCount; i++) {
            VkMemoryType type = typeBuffer.get(i);
            types.add(new MemoryType(type, heaps.get(type.heapIndex())));
        }

        return new PhysicalDevice(device, extensionCount, properties, limits, spareProperties, extensions, features, types);
    }
}
