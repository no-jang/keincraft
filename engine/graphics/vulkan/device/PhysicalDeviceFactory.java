package engine.graphics.vulkan.device;

import engine.graphics.vulkan.device.properties.DeviceLimits;
import engine.graphics.vulkan.device.properties.DeviceProperties;
import engine.graphics.vulkan.device.properties.DeviceSpareProperties;
import engine.graphics.vulkan.instance.Instance;
import engine.graphics.vulkan.memory.properties.MemoryHeap;
import engine.graphics.vulkan.memory.properties.MemoryType;
import engine.graphics.vulkan.util.function.VkFunction;
import engine.memory.MemoryContext;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkMemoryHeap;
import org.lwjgl.vulkan.VkMemoryType;
import org.lwjgl.vulkan.VkPhysicalDevice;
import org.lwjgl.vulkan.VkPhysicalDeviceMemoryProperties;
import org.lwjgl.vulkan.VkPhysicalDeviceProperties;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class PhysicalDeviceFactory {
    public static List<PhysicalDevice> createPhysicalDevices(Instance instance) {
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

    public static PhysicalDevice createPhysicalDevice(VkPhysicalDevice device) {
        MemoryStack stack = MemoryContext.getStack();

        VkPhysicalDeviceProperties vkProperties = VkPhysicalDeviceProperties.malloc(stack);
        VK10.vkGetPhysicalDeviceProperties(device, vkProperties);

        DeviceProperties properties = new DeviceProperties(vkProperties);
        DeviceLimits limits = new DeviceLimits(vkProperties.limits());
        List<DeviceSpareProperties> spareProperties = DeviceSpareProperties.ofVk(vkProperties.sparseProperties());

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

        return new PhysicalDevice(device, properties, limits, spareProperties, types);
    }
}
