package engine.graphics.vulkan.sync;

import engine.graphics.vulkan.device.Device;
import engine.helper.pointer.DestroyablePointer;
import org.lwjgl.vulkan.VK10;

public class Semaphore extends DestroyablePointer {
    private final Device device;

    public Semaphore(Device device, long handle) {
        super(handle);

        this.device = device;
    }

    @Override
    protected void destroy(long handle) {
        VK10.vkDestroySemaphore(device.getReference(), handle, null);
    }
}
