package engine.graphics.vulkan.surface.properties;

import engine.graphics.vulkan.image.properties.ColorSpace;
import engine.graphics.vulkan.image.properties.Format;
import engine.util.enums.HasValue;
import org.lwjgl.vulkan.VkSurfaceFormatKHR;

import java.util.Objects;

public class SurfaceFormat {
    private final Format format;
    private final ColorSpace colorSpace;

    public SurfaceFormat(VkSurfaceFormatKHR vkSurfaceFormat) {
        this.format = Objects.requireNonNull(HasValue.getByValue(vkSurfaceFormat.format(), Format.class));
        this.colorSpace = Objects.requireNonNull(HasValue.getByValue(vkSurfaceFormat.colorSpace(), ColorSpace.class));
    }

    public Format getFormat() {
        return format;
    }

    public ColorSpace getColorSpace() {
        return colorSpace;
    }

    @Override
    public String toString() {
        return "SurfaceFormat[" +
                "format=" + format +
                ", colorSpace=" + colorSpace +
                ']';
    }
}
