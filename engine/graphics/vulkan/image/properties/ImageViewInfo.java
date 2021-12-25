package engine.graphics.vulkan.image.properties;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;

public class ImageViewInfo {
    @Nullable
    private final ImageViewType viewType;
    @Nullable
    private final Collection<ImageAspect> aspects;

    public ImageViewInfo(@Nullable ImageViewType viewType, @Nullable Collection<ImageAspect> aspects) {
        this.viewType = viewType;
        this.aspects = aspects;
    }

    @Nullable
    public ImageViewType getViewType() {
        return viewType;
    }

    @Nullable
    public Collection<ImageAspect> getAspects() {
        return aspects;
    }

    public static class Builder {
        @Nullable
        private ImageViewType viewType;
        @Nullable
        private Collection<ImageAspect> aspects;

        public Builder viewType(@Nullable ImageViewType viewType) {
            this.viewType = viewType;
            return this;
        }

        public Builder aspects(@Nullable Collection<ImageAspect> aspects) {
            this.aspects = aspects;
            return this;
        }

        public ImageViewInfo build() {
            return new ImageViewInfo(viewType, aspects);
        }
    }
}
