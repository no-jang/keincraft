package engine.graphics.vulkan.properties;

import engine.util.enums.HasValue;
import org.lwjgl.vulkan.VK10;

public enum SharingMode implements HasValue<Integer> {
    EXCLUSIVE(VK10.VK_SHARING_MODE_EXCLUSIVE),
    CONCURRENT(VK10.VK_SHARING_MODE_CONCURRENT);

    private final int value;

    SharingMode(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
