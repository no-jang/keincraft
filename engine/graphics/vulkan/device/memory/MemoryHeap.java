package engine.graphics.vulkan.device.memory;

import engine.collections.set.DefaultImmutableSet;
import engine.collections.set.ImmutableSet;
import engine.util.enums.Maskable;
import org.lwjgl.vulkan.VkMemoryHeap;

public class MemoryHeap {
    private final long size;
    private final ImmutableSet<MemoryHeapFlag> flags;

    public MemoryHeap(VkMemoryHeap heap) {
        size = heap.size();
        flags = new DefaultImmutableSet<>(Maskable.fromBitMask(heap.flags(), MemoryHeapFlag.class));
    }

    public long getSize() {
        return size;
    }

    public ImmutableSet<MemoryHeapFlag> getFlags() {
        return flags;
    }

    @Override
    public String toString() {
        return "MemoryHeap[" +
                "size=" + size +
                ", flags=" + flags +
                ']';
    }
}
