package engine.graphics.vulkan.instance;

import engine.helper.pointer.DestroyableReferencePointer;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkInstance;

public class Instance extends DestroyableReferencePointer<VkInstance> {
    public Instance(VkInstance reference) {
        super(reference);
    }

    @Override
    protected void destroy(VkInstance reference) {
        VK10.vkDestroyInstance(reference, null);
    }
}
