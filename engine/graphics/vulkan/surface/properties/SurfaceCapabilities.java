package engine.graphics.vulkan.surface.properties;

import engine.collections.set.DefaultImmutableSet;
import engine.collections.set.ImmutableSet;
import engine.graphics.vulkan.image.properties.ImageUsage;
import engine.graphics.vulkan.properties.Extent2D;
import engine.helper.enums.Maskable;
import org.lwjgl.vulkan.VkSurfaceCapabilitiesKHR;

public class SurfaceCapabilities {
    private final int minImageCount;
    private final int maxImageCount;
    private final int maxImageArrayLayers;
    private final Extent2D currentExtent;
    private final Extent2D minImageExtent;
    private final Extent2D maxImageExtent;
    private final SurfaceTransform currentTransform;
    private final ImmutableSet<SurfaceTransform> supportedTransforms;
    private final ImmutableSet<CompositeAlpha> supportedCompositeAlphas;
    private final ImmutableSet<ImageUsage> supportedUsageFlags;

    public SurfaceCapabilities(VkSurfaceCapabilitiesKHR vkCapabilities) {
        minImageCount = vkCapabilities.minImageCount();
        maxImageCount = vkCapabilities.maxImageCount();
        maxImageArrayLayers = vkCapabilities.maxImageArrayLayers();
        currentExtent = new Extent2D(vkCapabilities.currentExtent());
        minImageExtent = new Extent2D(vkCapabilities.minImageExtent());
        maxImageExtent = new Extent2D(vkCapabilities.maxImageExtent());
        currentTransform = Maskable.fromBit(vkCapabilities.currentTransform(), SurfaceTransform.class);
        supportedTransforms = new DefaultImmutableSet<>(Maskable.fromBitMask(vkCapabilities.supportedTransforms(), SurfaceTransform.class));
        supportedCompositeAlphas = new DefaultImmutableSet<>(Maskable.fromBitMask(vkCapabilities.supportedCompositeAlpha(), CompositeAlpha.class));
        supportedUsageFlags = new DefaultImmutableSet<>(Maskable.fromBitMask(vkCapabilities.supportedUsageFlags(), ImageUsage.class));
    }

    public int getMinImageCount() {
        return minImageCount;
    }

    public int getMaxImageCount() {
        return maxImageCount;
    }

    public int getMaxImageArrayLayers() {
        return maxImageArrayLayers;
    }

    public Extent2D getCurrentExtent() {
        return currentExtent;
    }

    public Extent2D getMinImageExtent() {
        return minImageExtent;
    }

    public Extent2D getMaxImageExtent() {
        return maxImageExtent;
    }

    public SurfaceTransform getCurrentTransform() {
        return currentTransform;
    }

    public ImmutableSet<SurfaceTransform> getSupportedTransforms() {
        return supportedTransforms;
    }

    public ImmutableSet<CompositeAlpha> getSupportedCompositeAlphas() {
        return supportedCompositeAlphas;
    }

    public ImmutableSet<ImageUsage> getSupportedUsageFlags() {
        return supportedUsageFlags;
    }

    @Override
    public String toString() {
        return "SurfaceCapabilities[" +
                "minImageCount=" + minImageCount +
                ", maxImageCount=" + maxImageCount +
                ", maxImageArrayLayers=" + maxImageArrayLayers +
                ", currentExtent=" + currentExtent +
                ", minImageExtent=" + minImageExtent +
                ", maxImageExtent=" + maxImageExtent +
                ", currentTransform=" + currentTransform +
                ", supportedTransforms=" + supportedTransforms +
                ", supportedCompositeAlphas=" + supportedCompositeAlphas +
                ", supportedUsageFlags=" + supportedUsageFlags +
                ']';
    }
}
