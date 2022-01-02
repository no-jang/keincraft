package engine.graphics.instance;

import engine.ecs.entity.Entity;
import org.lwjgl.vulkan.VkInstance;

public class Instance extends Entity {
    private final VkInstance handle;

    public Instance(VkInstance handle) {
        this.handle = handle;
    }

    public VkInstance getHandle() {
        return handle;
    }
}
