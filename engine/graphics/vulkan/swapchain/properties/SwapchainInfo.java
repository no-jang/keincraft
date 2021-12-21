package engine.graphics.vulkan.swapchain.properties;

import engine.graphics.vulkan.properties.Extent2D;
import engine.graphics.vulkan.queue.QueueFamily;
import engine.graphics.vulkan.surface.properties.PresentMode;
import engine.graphics.vulkan.surface.properties.SurfaceFormat;
import engine.graphics.vulkan.surface.properties.SurfaceTransform;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SwapchainInfo {
    private final int minImageCount;
    @Nullable
    private final SurfaceFormat format;
    @Nullable
    private final Extent2D imageExtent;
    @Nullable
    private final SurfaceTransform surfaceTransform;
    @Nullable
    private final PresentMode presentMode;
    @Nullable
    private final QueueFamily graphicsFamily;
    @Nullable
    private final QueueFamily presentFamily;

    public SwapchainInfo(int minImageCount,
                         @Nullable SurfaceFormat format,
                         @Nullable Extent2D imageExtent,
                         @Nullable SurfaceTransform surfaceTransform,
                         @Nullable PresentMode presentMode,
                         @Nullable QueueFamily graphicsFamily,
                         @Nullable QueueFamily presentFamily) {
        this.minImageCount = minImageCount;
        this.format = format;
        this.imageExtent = imageExtent;
        this.surfaceTransform = surfaceTransform;
        this.presentMode = presentMode;
        this.graphicsFamily = graphicsFamily;
        this.presentFamily = presentFamily;
    }

    public int getMinImageCount() {
        return minImageCount;
    }

    @Nullable
    public SurfaceFormat getFormat() {
        return format;
    }

    @Nullable
    public Extent2D getImageExtent() {
        return imageExtent;
    }

    @Nullable
    public SurfaceTransform getSurfaceTransform() {
        return surfaceTransform;
    }

    @Nullable
    public PresentMode getPresentMode() {
        return presentMode;
    }

    @Nullable
    public QueueFamily getGraphicsFamily() {
        return graphicsFamily;
    }

    @Nullable
    public QueueFamily getPresentFamily() {
        return presentFamily;
    }

    public static class Builder {
        private int minImageCount;
        @Nullable
        private SurfaceFormat format;
        @Nullable
        private Extent2D imageExtent;
        @Nullable
        private SurfaceTransform surfaceTransform;
        @Nullable
        private PresentMode presentMode;
        @Nullable
        private QueueFamily graphicsFamily;
        @Nullable
        private QueueFamily presentFamily;

        public Builder() {
            this.minImageCount = -1;
        }

        public Builder minImageCount(int minImageCount) {
            this.minImageCount = minImageCount;
            return this;
        }

        public Builder format(@Nullable SurfaceFormat format) {
            this.format = format;
            return this;
        }

        public Builder imageExtent(@Nullable Extent2D imageExtent) {
            this.imageExtent = imageExtent;
            return this;
        }

        public Builder surfaceTransform(@Nullable SurfaceTransform surfaceTransform) {
            this.surfaceTransform = surfaceTransform;
            return this;
        }

        public Builder presentMode(@Nullable PresentMode presentMode) {
            this.presentMode = presentMode;
            return this;
        }

        public Builder graphicsQueueFamily(@Nullable QueueFamily graphicsFamily) {
            this.graphicsFamily = graphicsFamily;
            return this;
        }

        public Builder presentQueueFamily(@Nullable QueueFamily presentFamily) {
            this.presentFamily = presentFamily;
            return this;
        }

        public SwapchainInfo build() {
            return new SwapchainInfo(minImageCount, format, imageExtent, surfaceTransform, presentMode, graphicsFamily,
                    presentFamily);
        }
    }
}
