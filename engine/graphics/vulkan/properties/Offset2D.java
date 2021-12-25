package engine.graphics.vulkan.properties;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkOffset2D;

public class Offset2D {
    private final int x;
    private final int y;

    public Offset2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Offset2D(VkOffset2D vkOffset) {
        this(vkOffset.x(), vkOffset.y());
    }

    public VkOffset2D toVk(MemoryStack stack) {
        return VkOffset2D.malloc(stack)
                .x(x)
                .y(y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
