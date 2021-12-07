package client.graphics.vk.surface.models;

import client.graphics.vk.models.Maskable;
import org.lwjgl.vulkan.KHRSurface;

public enum CompositeAlpha implements Maskable {
    OPAQUE(KHRSurface.VK_COMPOSITE_ALPHA_OPAQUE_BIT_KHR),
    PRE_MULTIPLIED(KHRSurface.VK_COMPOSITE_ALPHA_PRE_MULTIPLIED_BIT_KHR),
    POST_MULTIPLIED(KHRSurface.VK_COMPOSITE_ALPHA_POST_MULTIPLIED_BIT_KHR),
    INHERIT(KHRSurface.VK_COMPOSITE_ALPHA_INHERIT_BIT_KHR);

    private final int bit;

    CompositeAlpha(int bit) {
        this.bit = bit;
    }

    @Override
    public int getBit() {
        return bit;
    }
}
