package engine.graphics.vulkan.sync;

import engine.graphics.vulkan.device.Device;
import engine.helper.pointer.DestroyablePointer;
import org.lwjgl.vulkan.VK10;

public class Fence extends DestroyablePointer {
    private final Device device;

    public Fence(Device device, long handle) {
        super(handle);

        this.device = device;
    }

    @Override
    protected void destroy(long handle) {
        VK10.vkDestroyFence(device.getReference(), handle, null);
    }

    public Device getDevice() {
        return device;
    }
}
