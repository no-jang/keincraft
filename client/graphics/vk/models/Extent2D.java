package client.graphics.vk.models;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkExtent2D;

public class Extent2D {
    private final int width;
    private final int height;

    public Extent2D(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Extent2D(VkExtent2D vkExtent) {
        this(vkExtent.width(), vkExtent.height());
    }

    public VkExtent2D toVulkanExtent(MemoryStack stack) {
        return VkExtent2D.malloc(stack)
                .width(width)
                .height(height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
