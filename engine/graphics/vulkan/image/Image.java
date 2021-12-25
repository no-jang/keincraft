package engine.graphics.vulkan.image;

import engine.collections.ImmutableCollection;
import engine.graphics.vulkan.device.Device;
import engine.graphics.vulkan.image.properties.ImageCreateFlag;
import engine.graphics.vulkan.image.properties.ImageLayout;
import engine.graphics.vulkan.image.properties.ImageTiling;
import engine.graphics.vulkan.image.properties.ImageType;
import engine.graphics.vulkan.image.properties.ImageUsage;
import engine.graphics.vulkan.properties.Extent3D;
import engine.graphics.vulkan.properties.Format;
import engine.graphics.vulkan.properties.SampleCount;
import engine.graphics.vulkan.properties.SharingMode;
import engine.graphics.vulkan.queue.QueueFamily;
import engine.util.pointer.Destroyable;
import org.lwjgl.vulkan.VK10;

import java.util.concurrent.atomic.AtomicBoolean;

public class Image extends ImageBase implements Destroyable {
    private final SampleCount sampleCount;
    private final ImageTiling tiling;
    private final SharingMode sharingMode;
    private final Format format;
    private final ImageLayout layout;
    private final ImmutableCollection<ImageUsage> usages;
    private final ImmutableCollection<QueueFamily> families;
    private final ImmutableCollection<ImageCreateFlag> flags;

    private final Device device;
    private final AtomicBoolean isDisposed;

    public Image(long handle,
                 Device device,
                 int mipLevelCount,
                 int arrayLayerCount,
                 Extent3D extent,
                 Format format,
                 ImageType type,
                 SampleCount sampleCount,
                 ImageTiling tiling,
                 SharingMode sharingMode,
                 ImageLayout layout,
                 ImmutableCollection<ImageUsage> usages,
                 ImmutableCollection<QueueFamily> families,
                 ImmutableCollection<ImageCreateFlag> flags) {
        super(handle, mipLevelCount, arrayLayerCount, extent, format, type);
        this.sampleCount = sampleCount;
        this.tiling = tiling;
        this.sharingMode = sharingMode;
        this.format = format;
        this.layout = layout;
        this.usages = usages;
        this.families = families;
        this.flags = flags;

        this.device = device;
        this.isDisposed = new AtomicBoolean(false);
    }

    @Override
    public long getHandle() {
        if (isDisposed.get()) {
            throw new IllegalStateException("Underlying pointer has already been destroyed");
        }

        return super.getHandle();
    }

    @Override
    public void destroy() {
        if (isDisposed.getAndSet(true)) {
            return;
        }

        VK10.vkDestroyImage(device.getReference(), handle, null);
    }

    public SampleCount getSampleCount() {
        return sampleCount;
    }

    public ImageTiling getTiling() {
        return tiling;
    }

    public SharingMode getSharingMode() {
        return sharingMode;
    }

    @Override
    public Format getFormat() {
        return format;
    }

    public ImageLayout getLayout() {
        return layout;
    }

    public ImmutableCollection<ImageUsage> getUsages() {
        return usages;
    }

    public ImmutableCollection<QueueFamily> getFamilies() {
        return families;
    }

    public ImmutableCollection<ImageCreateFlag> getFlags() {
        return flags;
    }
}
