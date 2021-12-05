package client.graphics.vk.device.models.memory;

import client.graphics.vk.models.Maskable;
import org.lwjgl.vulkan.VkMemoryHeap;

import java.util.Set;

public class MemoryHeap {
    private final long size;
    private final Set<MemoryHeapFlag> flags;

    public MemoryHeap(VkMemoryHeap vkMemoryHeap) {
        size = vkMemoryHeap.size();
        flags = Maskable.fromBitMask(vkMemoryHeap.flags(), MemoryHeapFlag.class);
    }

    public long getSize() {
        return size;
    }

    public Set<MemoryHeapFlag> getFlags() {
        return flags;
    }

    @Override
    public String toString() {
        return "[" +
                "size=" + size +
                ", flags=" + flags +
                "]";
    }
}
