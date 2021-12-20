package engine.graphics.vulkan.queue.properties;

import engine.helper.enums.Maskable;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VK11;

public enum QueueCapability implements Maskable {
    GRAPHICS(VK10.VK_QUEUE_GRAPHICS_BIT),
    COMPUTE(VK10.VK_QUEUE_COMPUTE_BIT),
    TRANSFER(VK10.VK_QUEUE_TRANSFER_BIT),
    SPARSE_BINDING(VK10.VK_QUEUE_SPARSE_BINDING_BIT),
    PROTECTED(VK11.VK_QUEUE_PROTECTED_BIT);

    private final int bit;

    QueueCapability(int bit) {
        this.bit = bit;
    }

    @Override
    public int getBit() {
        return bit;
    }
}