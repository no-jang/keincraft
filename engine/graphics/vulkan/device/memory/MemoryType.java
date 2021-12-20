package engine.graphics.vulkan.device.memory;

import engine.collections.set.DefaultImmutableSet;
import engine.collections.set.ImmutableSet;
import engine.helper.enums.Maskable;
import org.lwjgl.vulkan.VkMemoryType;

public class MemoryType {
    private final int index;
    private final MemoryHeap heap;
    private final ImmutableSet<MemoryProperty> properties;

    public MemoryType(VkMemoryType type, MemoryHeap heap) {
        this.index = type.heapIndex();
        this.heap = heap;
        this.properties = new DefaultImmutableSet<>(Maskable.fromBitMask(type.propertyFlags(), MemoryProperty.class));
    }

    public int getIndex() {
        return index;
    }

    public MemoryHeap getHeap() {
        return heap;
    }

    public ImmutableSet<MemoryProperty> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "MemoryType[" +
                "index=" + index +
                ", heap=" + heap +
                ", properties=" + properties +
                ']';
    }
}
