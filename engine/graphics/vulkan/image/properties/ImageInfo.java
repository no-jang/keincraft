package engine.graphics.vulkan.image.properties;

import engine.collections.DefaultImmutableCollection;
import engine.collections.ImmutableCollection;
import engine.graphics.vulkan.properties.Extent3D;
import engine.graphics.vulkan.properties.Format;
import engine.graphics.vulkan.properties.SampleCount;
import engine.graphics.vulkan.properties.SharingMode;
import engine.graphics.vulkan.queue.QueueFamily;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Arrays;
import java.util.Collection;

public class ImageInfo {
    private final int mipLevelCount;
    private final int arrayLayerCount;
    @Nullable
    private final Format format;
    @Nullable
    private final Extent3D extent;
    @Nullable
    private final ImageType type;
    @Nullable
    private final SampleCount sampleCount;
    @Nullable
    private final ImageTiling tiling;
    @Nullable
    private final SharingMode sharingMode;
    @Nullable
    private final ImageLayout layout;
    @Nullable
    private final ImmutableCollection<ImageUsage> usages;
    @Nullable
    private final ImmutableCollection<QueueFamily> families;
    @Nullable
    private final ImmutableCollection<ImageCreateFlag> flags;

    public ImageInfo(int mipLevelCount,
                     int arrayLayerCount,
                     @Nullable Format format,
                     @Nullable Extent3D extent,
                     @Nullable ImageType type,
                     @Nullable SampleCount sampleCount,
                     @Nullable ImageTiling tiling,
                     @Nullable SharingMode sharingMode,
                     @Nullable ImageLayout layout,
                     @Nullable Collection<ImageUsage> usages,
                     @Nullable Collection<QueueFamily> families,
                     @Nullable Collection<ImageCreateFlag> flags) {
        this.mipLevelCount = mipLevelCount;
        this.arrayLayerCount = arrayLayerCount;
        this.format = format;
        this.extent = extent;
        this.type = type;
        this.sampleCount = sampleCount;
        this.tiling = tiling;
        this.sharingMode = sharingMode;
        this.layout = layout;

        if (usages != null) {
            this.usages = new DefaultImmutableCollection<>(usages);
        } else {
            this.usages = null;
        }

        if (families != null) {
            this.families = new DefaultImmutableCollection<>(families);
        } else {
            this.families = null;
        }

        if (flags != null) {
            this.flags = new DefaultImmutableCollection<>(flags);
        } else {
            this.flags = null;
        }
    }

    public int getMipLevelCount() {
        return mipLevelCount;
    }

    public int getArrayLayerCount() {
        return arrayLayerCount;
    }

    @Nullable
    public Format getFormat() {
        return format;
    }

    @Nullable
    public Extent3D getExtent() {
        return extent;
    }

    @Nullable
    public ImageType getType() {
        return type;
    }

    @Nullable
    public SampleCount getSampleCount() {
        return sampleCount;
    }

    @Nullable
    public ImageTiling getTiling() {
        return tiling;
    }

    @Nullable
    public SharingMode getSharingMode() {
        return sharingMode;
    }

    @Nullable
    public ImageLayout getLayout() {
        return layout;
    }

    @Nullable
    public ImmutableCollection<ImageUsage> getUsages() {
        return usages;
    }

    @Nullable
    public ImmutableCollection<QueueFamily> getFamilies() {
        return families;
    }

    @Nullable
    public ImmutableCollection<ImageCreateFlag> getFlags() {
        return flags;
    }

    public static class Builder {
        private int mipLevelCount;
        private int arrayLayerCount;
        @Nullable
        private Format format;
        @Nullable
        private Extent3D extent;
        @Nullable
        private ImageType type;
        @Nullable
        private SampleCount sampleCount;
        @Nullable
        private ImageTiling tiling;
        @Nullable
        private SharingMode sharingMode;
        @Nullable
        private ImageLayout layout;
        @Nullable
        private Collection<ImageUsage> usages;
        @Nullable
        private Collection<QueueFamily> families;
        @Nullable
        private Collection<ImageCreateFlag> flags;

        public Builder() {
            mipLevelCount = -1;
            arrayLayerCount = -1;
        }

        public Builder mipLevelCount(int mipLevelCount) {
            this.mipLevelCount = mipLevelCount;
            return this;
        }

        public Builder arrayLayerCount(int arrayLayerCount) {
            this.arrayLayerCount = arrayLayerCount;
            return this;
        }

        public Builder format(Format format) {
            this.format = format;
            return this;
        }

        public Builder extent(Extent3D extent) {
            this.extent = extent;
            return this;
        }

        public Builder type(ImageType type) {
            this.type = type;
            return this;
        }

        public Builder sampleCount(SampleCount sampleCount) {
            this.sampleCount = sampleCount;
            return this;
        }

        public Builder tiling(ImageTiling tiling) {
            this.tiling = tiling;
            return this;
        }

        public Builder sharingMode(SharingMode sharingMode) {
            this.sharingMode = sharingMode;
            return this;
        }

        public Builder layout(ImageLayout layout) {
            this.layout = layout;
            return this;
        }

        public Builder usages(ImageUsage... usages) {
            this.usages = Arrays.asList(usages);
            return this;
        }

        public Builder usages(Collection<ImageUsage> usages) {
            this.usages = usages;
            return this;
        }

        public Builder queueFamilies(QueueFamily... families) {
            this.families = Arrays.asList(families);
            return this;
        }

        public Builder queueFamilies(Collection<QueueFamily> families) {
            this.families = families;
            return this;
        }

        public Builder flags(ImageCreateFlag... flags) {
            this.flags = Arrays.asList(flags);
            return this;
        }

        public Builder flags(Collection<ImageCreateFlag> flags) {
            this.flags = flags;
            return this;
        }

        public ImageInfo build() {
            return new ImageInfo(mipLevelCount,
                    arrayLayerCount,
                    format,
                    extent,
                    type,
                    sampleCount,
                    tiling,
                    sharingMode,
                    layout,
                    usages,
                    families,
                    flags);
        }
    }
}
