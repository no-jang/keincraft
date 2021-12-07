package client.graphics.vk.queue;

import client.graphics.vk.models.pointers.ReferencePointer;
import org.lwjgl.vulkan.VkQueue;

// TODO Present and submit
public class Queue extends ReferencePointer<VkQueue> {
    private final VkQueue handle;

    public Queue(VkQueue handle) {
        this.handle = handle;
    }

    @Override
    protected long getInternalHandle() {
        return handle.address();
    }

    @Override
    protected VkQueue getInternalReference() {
        return handle;
    }
}
