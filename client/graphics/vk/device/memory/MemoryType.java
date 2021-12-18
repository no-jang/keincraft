package client.graphics.vk.device.memory;

import common.util.enums.Maskable;
import org.lwjgl.vulkan.VkMemoryType;

import java.util.Set;

/**
 * Properties specifying one type of memory in the graphics device
 */
public class MemoryType {
    private final int index;
    private final MemoryHeap heap;
    private final Set<MemoryProperty> properties;

    /**
     * Normally only called by {@link client.graphics.vk.device.PhysicalDevice} during property recognition
     *
     * @param vkMemoryType {@link VkMemoryType}
     * @param heap {@link MemoryHeap}
     */
    public MemoryType(VkMemoryType vkMemoryType, MemoryHeap heap) {
        this.index = vkMemoryType.heapIndex();
        this.properties = Maskable.fromBitMask(vkMemoryType.propertyFlags(), MemoryProperty.class);
        this.heap = heap;
    }

    /**
     * Index of this type of memory used as an identifier.
     *
     * @return index of memory type
     */
    public int getIndex() {
        return index;
    }

    /**
     * Corresponding {@link MemoryHeap} section in this type of memory
     *
     * @return {@link MemoryHeap}
     */
    public MemoryHeap getHeap() {
        return heap;
    }

    /**
     * {@link MemoryProperty}s describing this memory type
     *
     * @return memory type {@link MemoryProperty}s
     */
    public Set<MemoryProperty> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "[" +
                "index=" + index +
                ", heap=" + heap +
                ", properties=" + properties +
                ']';
    }
}
