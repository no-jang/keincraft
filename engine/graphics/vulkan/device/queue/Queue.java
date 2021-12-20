package engine.graphics.vulkan.device.queue;

import engine.helper.pointer.ReferencePointer;
import org.lwjgl.vulkan.VkQueue;

public class Queue extends ReferencePointer<VkQueue> {
    public Queue(VkQueue reference) {
        super(reference);
    }
}
