package engine.graphics.vulkan.memory;

import engine.graphics.vulkan.device.Device;
import engine.graphics.vulkan.helper.function.VkFunction;
import engine.helper.pointer.DestroyablePointer;
import engine.memory.MemoryContext;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkMappedMemoryRange;

import java.nio.ByteBuffer;

public class MappedDeviceMemory extends DestroyablePointer {
    private final Device device;
    private final DeviceMemory memory;
    private final boolean isCoherent;

    public MappedDeviceMemory(Device device, DeviceMemory memory, long handle, boolean isCoherent) {
        super(handle);

        this.device = device;
        this.memory = memory;
        this.isCoherent = isCoherent;
    }

    @Override
    protected void destroy(long handle) {
        VK10.vkUnmapMemory(device.getReference(), memory.getHandle());
        memory.unmap();
    }

    public void flush() {
        if (isCoherent) {
            return;
        }

        MemoryStack stack = MemoryContext.getStack();

        VkMappedMemoryRange mappedMemoryRange = VkMappedMemoryRange.malloc(stack)
                .sType$Default()
                .pNext(0)
                .memory(memory.getHandle())
                .offset(0L)
                .size(VK10.VK_WHOLE_SIZE);

        VkFunction.execute(() -> VK10.vkFlushMappedMemoryRanges(device.getReference(), mappedMemoryRange));
    }

    public ByteBuffer getBuffer() {
        return MemoryUtil.memByteBuffer(handle, Math.toIntExact(memory.getSize()));
    }

    public Device getDevice() {
        return device;
    }

    public DeviceMemory getMemory() {
        return memory;
    }

    public boolean isCoherent() {
        return isCoherent;
    }

    @Override
    public String toString() {
        return "MappedDeviceMemory[" +
                "memory=" + memory.toSimpleString() +
                ", isCoherent=" + isCoherent +
                ", handle=" + handle +
                ']';
    }
}
