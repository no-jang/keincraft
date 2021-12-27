package engine.graphics.vulkan.queue;

import engine.graphics.vulkan.device.Device;
import engine.memory.test.ownable.OwnableStruct;
import org.lwjgl.vulkan.VkQueue;

public class Queue extends OwnableStruct<Device, VkQueue> {
    private final QueueFamily family;
    private final int index;

    public Queue(Device owner,
                 VkQueue reference,
                 QueueFamily family,
                 int index) {
        super(owner, reference, reference.address());
        this.family = family;
        this.index = index;
    }

    public QueueFamily getFamily() {
        return family;
    }

    public int getIndex() {
        return index;
    }
}
