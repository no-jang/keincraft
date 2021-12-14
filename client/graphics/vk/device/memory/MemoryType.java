package client.graphics.vk.device.memory;

import common.util.enums.Maskable;
import org.lwjgl.vulkan.VkMemoryType;

import java.util.Set;

public class MemoryType {
    private final int index;
    private final MemoryHeap heap;
    private final Set<MemoryProperty> properties;

    public MemoryType(VkMemoryType vkMemoryType, MemoryHeap heap) {
        this.index = vkMemoryType.heapIndex();
        this.properties = Maskable.fromBitMask(vkMemoryType.propertyFlags(), MemoryProperty.class);
        this.heap = heap;
    }

    public int getIndex() {
        return index;
    }

    public MemoryHeap getHeap() {
        return heap;
    }

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
