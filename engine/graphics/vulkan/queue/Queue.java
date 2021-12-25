package engine.graphics.vulkan.queue;

import engine.graphics.vulkan.device.Device;
import engine.util.pointer.ReferencePointer;
import org.lwjgl.vulkan.VkQueue;

public class Queue extends ReferencePointer<VkQueue> {
    private final Device device;
    private final QueueFamily family;
    private final int index;

    public Queue(VkQueue reference,
                 Device device,
                 QueueFamily family,
                 int index) {
        super(reference);
        this.device = device;
        this.family = family;
        this.index = index;
    }

    public Device getDevice() {
        return device;
    }

    public QueueFamily getFamily() {
        return family;
    }

    public int getIndex() {
        return index;
    }
}
