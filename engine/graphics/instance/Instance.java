package engine.graphics.instance;

import org.lwjgl.vulkan.VkInstance;

public class Instance {
    private final VkInstance handle;

    public Instance(VkInstance handle) {
        this.handle = handle;
    }

    public VkInstance getHandle() {
        return handle;
    }
}
