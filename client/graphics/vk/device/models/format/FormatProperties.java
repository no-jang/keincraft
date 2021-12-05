package client.graphics.vk.device.models.format;

import client.graphics.vk.models.Maskable;
import org.lwjgl.vulkan.VkFormatProperties;

import java.util.Set;

public class FormatProperties {
    private final Set<FormatFeature> linearTilingFeatures;
    private final Set<FormatFeature> optimalTilingFeatures;
    private final Set<FormatFeature> bufferFeatures;

    public FormatProperties(VkFormatProperties vkFormatProperties) {
        linearTilingFeatures = Maskable.fromBitMask(vkFormatProperties.linearTilingFeatures(), FormatFeature.class);
        optimalTilingFeatures = Maskable.fromBitMask(vkFormatProperties.optimalTilingFeatures(), FormatFeature.class);
        bufferFeatures = Maskable.fromBitMask(vkFormatProperties.bufferFeatures(), FormatFeature.class);
    }

    public Set<FormatFeature> getLinearTilingFeatures() {
        return linearTilingFeatures;
    }

    public Set<FormatFeature> getOptimalTilingFeatures() {
        return optimalTilingFeatures;
    }

    public Set<FormatFeature> getBufferFeatures() {
        return bufferFeatures;
    }

    @Override
    public String toString() {
        return "[" +
                "linearTilingFeatures=" + linearTilingFeatures +
                ", optimalTilingFeatures=" + optimalTilingFeatures +
                ", bufferFeatures=" + bufferFeatures +
                ']';
    }
}
