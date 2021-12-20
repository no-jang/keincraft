package engine.graphics.vulkan.device.properties;

import engine.collections.set.DefaultImmutableSet;
import engine.collections.set.ImmutableSet;
import engine.helper.enums.Maskable;
import org.lwjgl.vulkan.VkFormatProperties;

public class FormatProperties {
    private final ImmutableSet<FormatFeature> linearTilingFeatures;
    private final ImmutableSet<FormatFeature> optionalTilingFeatures;
    private final ImmutableSet<FormatFeature> bufferFeatures;

    public FormatProperties(VkFormatProperties formatProperties) {
        linearTilingFeatures = new DefaultImmutableSet<>(Maskable.fromBitMask(formatProperties.linearTilingFeatures(), FormatFeature.class));
        optionalTilingFeatures = new DefaultImmutableSet<>(Maskable.fromBitMask(formatProperties.optimalTilingFeatures(), FormatFeature.class));
        bufferFeatures = new DefaultImmutableSet<>(Maskable.fromBitMask(formatProperties.bufferFeatures(), FormatFeature.class));
    }

    public ImmutableSet<FormatFeature> getLinearTilingFeatures() {
        return linearTilingFeatures;
    }

    public ImmutableSet<FormatFeature> getOptionalTilingFeatures() {
        return optionalTilingFeatures;
    }

    public ImmutableSet<FormatFeature> getBufferFeatures() {
        return bufferFeatures;
    }

    @Override
    public String toString() {
        return "FormatProperties[" +
                "linearTilingFeatures=" + linearTilingFeatures +
                ", optionalTilingFeatures=" + optionalTilingFeatures +
                ", bufferFeatures=" + bufferFeatures +
                ']';
    }
}
