package engine.graphics.vulkan.image;

import engine.graphics.vulkan.image.properties.ImageType;
import engine.graphics.vulkan.properties.Extent3D;
import engine.graphics.vulkan.properties.Format;
import engine.util.pointer.Pointer;

public abstract class ImageBase extends Pointer {
    private final int mipLevelCount;
    private final int arrayLayerCount;
    private final Extent3D extent;
    private final Format format;
    private final ImageType type;

    public ImageBase(long handle,
                     int mipLevelCount,
                     int arrayLayerCount,
                     Extent3D extent,
                     Format format,
                     ImageType type) {
        super(handle);
        this.mipLevelCount = mipLevelCount;
        this.arrayLayerCount = arrayLayerCount;
        this.extent = extent;
        this.format = format;
        this.type = type;
    }

    public int getMipLevelCount() {
        return mipLevelCount;
    }

    public int getArrayLayerCount() {
        return arrayLayerCount;
    }

    public Extent3D getExtent() {
        return extent;
    }

    public Format getFormat() {
        return format;
    }

    public ImageType getType() {
        return type;
    }
}
