package engine.graphics.vulkan.image.properties;

import engine.util.enums.HasValue;
import org.lwjgl.vulkan.VK10;

public enum ImageType implements HasValue<Integer> {
    ONE_DIMENSIONAL(VK10.VK_IMAGE_TYPE_1D),
    TWO_DIMENSIONAL(VK10.VK_IMAGE_TYPE_2D),
    THREE_DIMENSIONAL(VK10.VK_IMAGE_TYPE_3D);

    private final int value;

    ImageType(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
