package client.graphics.vk.device.models.properties;

import org.lwjgl.vulkan.VkPhysicalDeviceLimits;

public class DeviceLimits {
    private final int maxColorAttachments;

    public DeviceLimits(VkPhysicalDeviceLimits limits) {
        maxColorAttachments = limits.maxColorAttachments();
    }

    public int getMaxColorAttachments() {
        return maxColorAttachments;
    }

    @Override
    public String toString() {
        return "limits: \n" +
                "- maxColorAttachments: " + maxColorAttachments;
    }
}
