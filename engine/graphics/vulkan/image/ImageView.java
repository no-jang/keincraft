package engine.graphics.vulkan.image;

import engine.graphics.vulkan.device.Device;
import engine.graphics.vulkan.image.properties.ImageAspect;
import engine.graphics.vulkan.image.properties.ImageViewType;
import engine.util.pointer.DestroyablePointer;
import org.lwjgl.vulkan.VK10;

import java.util.Collection;

public class ImageView extends DestroyablePointer {
    private final Device device;
    private final ImageBase image;
    private final ImageViewType type;
    private final Collection<ImageAspect> aspects;

    public ImageView(long handle, Device device, ImageBase image, ImageViewType type, Collection<ImageAspect> aspects) {
        super(handle);
        this.device = device;
        this.image = image;
        this.type = type;
        this.aspects = aspects;
    }

    @Override
    protected void destroy(long handle) {
        VK10.vkDestroyImageView(device.getReference(), handle, null);
    }

    public Device getDevice() {
        return device;
    }

    public ImageBase getImage() {
        return image;
    }

    public ImageViewType getType() {
        return type;
    }

    public Collection<ImageAspect> getAspects() {
        return aspects;
    }
}
