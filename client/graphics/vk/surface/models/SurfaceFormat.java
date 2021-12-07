package client.graphics.vk.surface.models;

import client.graphics.vk.image.models.ColorSpace;
import client.graphics.vk.image.models.Format;
import client.graphics.vk.models.HasValue;
import org.lwjgl.vulkan.VkSurfaceFormatKHR;

public class SurfaceFormat {
    private final Format format;
    private final ColorSpace colorSpace;

    public SurfaceFormat(VkSurfaceFormatKHR vkSurfaceFormat) {
        this.format = HasValue.getByValue(vkSurfaceFormat.format(), Format.class);
        this.colorSpace = HasValue.getByValue(vkSurfaceFormat.colorSpace(), ColorSpace.class);
    }

    public Format getFormat() {
        return format;
    }

    public ColorSpace getColorSpace() {
        return colorSpace;
    }
}
