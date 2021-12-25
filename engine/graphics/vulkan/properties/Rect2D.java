package engine.graphics.vulkan.properties;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkExtent2D;
import org.lwjgl.vulkan.VkOffset2D;
import org.lwjgl.vulkan.VkRect2D;

public class Rect2D {
    private final Extent2D extent;
    private final Offset2D offset;

    public Rect2D(Extent2D extent, Offset2D offset) {
        this.extent = extent;
        this.offset = offset;
    }

    public Rect2D(int x, int y, int width, int height) {
        this.extent = new Extent2D(x, y);
        this.offset = new Offset2D(width, height);
    }

    public Rect2D(VkRect2D vkRect) {
        this(new Extent2D(vkRect.extent()), new Offset2D(vkRect.offset()));
    }

    public Rect2D(VkExtent2D vkExtent, VkOffset2D vkOffset) {
        this(new Extent2D(vkExtent), new Offset2D(vkOffset));
    }

    public VkRect2D toVk(MemoryStack stack) {
        return VkRect2D.malloc(stack)
                .extent(extent.toVk(stack))
                .offset(offset.toVk(stack));
    }

    public Extent2D getExtent() {
        return extent;
    }

    public Offset2D getOffset() {
        return offset;
    }
}
