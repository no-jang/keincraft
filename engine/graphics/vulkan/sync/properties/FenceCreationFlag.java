package engine.graphics.vulkan.sync.properties;

import engine.util.enums.Maskable;
import org.lwjgl.vulkan.VK10;

public enum FenceCreationFlag implements Maskable {
    SIGNALED(VK10.VK_FENCE_CREATE_SIGNALED_BIT);

    private final int bit;

    FenceCreationFlag(int bit) {
        this.bit = bit;
    }

    @Override
    public int getBit() {
        return bit;
    }
}
