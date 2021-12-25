package engine.graphics.vulkan.image.properties;

import engine.util.enums.Maskable;
import org.lwjgl.vulkan.VK10;

public enum ImageAspect implements Maskable {
    COLOR(VK10.VK_IMAGE_ASPECT_COLOR_BIT),
    DEPTH(VK10.VK_IMAGE_ASPECT_DEPTH_BIT),
    STENCIL(VK10.VK_IMAGE_ASPECT_STENCIL_BIT),
    METADATA(VK10.VK_IMAGE_ASPECT_METADATA_BIT);

    private final int bit;

    ImageAspect(int bit) {
        this.bit = bit;
    }

    @Override
    public int getBit() {
        return bit;
    }
}
