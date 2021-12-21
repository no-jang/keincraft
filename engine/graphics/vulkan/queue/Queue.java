package engine.graphics.vulkan.queue;

import engine.util.pointer.ReferencePointer;
import org.lwjgl.vulkan.VkQueue;

public class Queue extends ReferencePointer<VkQueue> {
    public Queue(VkQueue reference) {
        super(reference);
    }
}
