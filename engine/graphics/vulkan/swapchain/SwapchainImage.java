package engine.graphics.vulkan.swapchain;

import engine.graphics.vulkan.image.ImageBase;
import engine.graphics.vulkan.image.properties.ImageType;
import engine.graphics.vulkan.properties.Extent2D;
import engine.graphics.vulkan.properties.Extent3D;
import engine.graphics.vulkan.properties.Format;

public class SwapchainImage extends ImageBase {
    public SwapchainImage(long handle, int mipLevelCount, int arrayLayerCount, Extent2D extent, Format format) {
        super(handle, mipLevelCount, arrayLayerCount, new Extent3D(extent), format, ImageType.TWO_DIMENSIONAL);
    }
}
