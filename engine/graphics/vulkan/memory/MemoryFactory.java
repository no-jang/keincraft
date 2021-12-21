package engine.graphics.vulkan.memory;

import engine.graphics.vulkan.device.Device;
import engine.graphics.vulkan.device.memory.MemoryProperty;
import engine.graphics.vulkan.device.memory.MemoryType;
import engine.graphics.vulkan.util.function.VkFunction;
import engine.memory.MemoryContext;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkMemoryAllocateInfo;

import java.nio.LongBuffer;

public class MemoryFactory {
    public static DeviceMemory createMemory(Device device, MemoryType type, long size) {
        if (!device.getPhysicalDevice().getMemoryTypes().contains(type)) {
            throw new IllegalArgumentException("Device does not have memory type " + type + " supported");
        }

        MemoryStack stack = MemoryContext.getStack();

        VkMemoryAllocateInfo allocateInfo = VkMemoryAllocateInfo.malloc(stack)
                .sType$Default()
                .pNext(0)
                .allocationSize(size)
                .memoryTypeIndex(type.getIndex());

        LongBuffer handleBuffer = stack.mallocLong(1);
        VkFunction.execute(() -> VK10.vkAllocateMemory(device.getReference(), allocateInfo, null, handleBuffer));
        return new DeviceMemory(device, type, handleBuffer.get(0), size);
    }

    public static MappedDeviceMemory createMappedMemory(DeviceMemory memory, long size, long offset) {
        MemoryStack stack = MemoryContext.getStack();

        if (memory.getMappedMemory() != null) {
            throw new IllegalArgumentException("Device memory is already mapped " + memory);
        }

        PointerBuffer handleBuffer = stack.mallocPointer(1);
        VkFunction.execute(() -> VK10.vkMapMemory(memory.getDevice().getReference(), memory.getHandle(), offset, size, 0, handleBuffer));

        boolean isCoherent = memory.getType().getProperties().contains(MemoryProperty.HOST_COHERENT);

        MappedDeviceMemory mappedMemory = new MappedDeviceMemory(memory.getDevice(), memory, handleBuffer.get(0), isCoherent);
        memory.map(mappedMemory);
        return mappedMemory;
    }
}
