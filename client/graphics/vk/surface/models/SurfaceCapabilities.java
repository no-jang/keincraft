package client.graphics.vk.surface.models;

import client.graphics.vk.image.models.ImageUsage;
import client.graphics.vk.models.Extent2D;
import client.graphics.vk.models.Maskable;
import org.lwjgl.vulkan.VkSurfaceCapabilitiesKHR;

import java.util.Set;

public class SurfaceCapabilities {
    private final int minImageCount;
    private final int maxImageCount;
    private final int maxImageArrayLayers;
    private final Extent2D currentExtent;
    private final Extent2D minImageExtent;
    private final Extent2D maxImageExtent;
    private final SurfaceTransform currentTransform;
    private final Set<SurfaceTransform> supportedTransforms;
    private final Set<CompositeAlpha> supportedCompositeAlphas;
    private final Set<ImageUsage> supportedUsageFlags;

    public SurfaceCapabilities(VkSurfaceCapabilitiesKHR vkCapabilities) {
        minImageCount = vkCapabilities.minImageCount();
        maxImageCount = vkCapabilities.maxImageCount();
        maxImageArrayLayers = vkCapabilities.maxImageArrayLayers();
        currentExtent = new Extent2D(vkCapabilities.currentExtent());
        minImageExtent = new Extent2D(vkCapabilities.minImageExtent());
        maxImageExtent = new Extent2D(vkCapabilities.maxImageExtent());
        currentTransform = Maskable.fromBit(vkCapabilities.currentTransform(), SurfaceTransform.class);
        supportedTransforms = Maskable.fromBitMask(vkCapabilities.supportedTransforms(), SurfaceTransform.class);
        supportedCompositeAlphas = Maskable.fromBitMask(vkCapabilities.supportedCompositeAlpha(), CompositeAlpha.class);
        supportedUsageFlags = Maskable.fromBitMask(vkCapabilities.supportedUsageFlags(), ImageUsage.class);
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

    public Set<SurfaceTransform> getSupportedTransforms() {
        return supportedTransforms;
    }

    public Set<CompositeAlpha> getSupportedCompositeAlphas() {
        return supportedCompositeAlphas;
    }

    public Set<ImageUsage> getSupportedUsageFlags() {
        return supportedUsageFlags;
    }
}
