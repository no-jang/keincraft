package engine.graphics.vulkan.properties;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkOffset3D;

public class Offset3D {
    private final int x;
    private final int y;
    private final int z;

    public Offset3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Offset3D(VkOffset3D vkOffset) {
        this(vkOffset.x(), vkOffset.y(), vkOffset.z());
    }

    public VkOffset3D toVk(MemoryStack stack) {
        return VkOffset3D.malloc(stack)
                .x(x)
                .y(y)
                .z(z);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
