package client.graphics.vk.device.memory;

import common.util.enums.Maskable;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VK11;

public enum MemoryHeapFlag implements Maskable {
    DEVICE_LOCAL(VK10.VK_MEMORY_HEAP_DEVICE_LOCAL_BIT),
    MULTI_INSTANCE(VK11.VK_MEMORY_HEAP_MULTI_INSTANCE_BIT);

    private final int bit;

    MemoryHeapFlag(int bit) {
        this.bit = bit;
    }

    @Override
    public int getBit() {
        return bit;
    }
}
