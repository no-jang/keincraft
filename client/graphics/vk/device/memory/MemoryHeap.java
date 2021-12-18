package client.graphics.vk.device.memory;

import common.util.enums.Maskable;
import org.lwjgl.vulkan.VkMemoryHeap;

import java.util.Set;

/**
 * Heap section of a graphical devices memory
 */
public class MemoryHeap {
    private final long size;
    private final Set<MemoryHeapFlag> flags;

    /**
     * Normally only called by {@link client.graphics.vk.device.PhysicalDevice} property recognition
     *
     * @param vkMemoryHeap {@link VkMemoryHeap}
     */
    public MemoryHeap(VkMemoryHeap vkMemoryHeap) {
        size = vkMemoryHeap.size();
        flags = Maskable.fromBitMask(vkMemoryHeap.flags(), MemoryHeapFlag.class);
    }

    /**
     * The size of this memory heap
     *
     * @return memory heap size
     */
    public long getSize() {
        return size;
    }

    /**
     * {@link MemoryHeapFlag}s specifying the attributes of this memory heap
     *
     * @return {@link MemoryHeapFlag}s
     */
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
