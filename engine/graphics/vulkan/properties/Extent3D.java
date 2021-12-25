package engine.graphics.vulkan.properties;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkExtent3D;

public class Extent3D {
    private final int width;
    private final int height;
    private final int depth;

    public Extent3D(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public Extent3D(VkExtent3D vkExtent) {
        this(vkExtent.width(), vkExtent.height(), vkExtent.depth());
    }

    public Extent3D(Extent2D extent) {
        this.width = extent.getWidth();
        this.height = extent.getHeight();
        this.depth = 0;
    }

    public VkExtent3D toVk(MemoryStack stack) {
        return VkExtent3D.malloc(stack)
                .width(width)
                .height(height)
                .depth(depth);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }
}
