package engine.graphics.vulkan.memory;

import engine.graphics.vulkan.device.Device;
import engine.graphics.vulkan.device.memory.MemoryType;
import engine.util.pointer.DestroyablePointer;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.lwjgl.vulkan.VK10;

import java.util.concurrent.atomic.AtomicReference;

public class DeviceMemory extends DestroyablePointer {
    private final Device device;
    private final MemoryType type;
    private final long size;

    private final AtomicReference<MappedDeviceMemory> mappedDeviceMemory;

    public DeviceMemory(Device device, MemoryType type, long handle, long size) {
        super(handle);

        this.device = device;
        this.type = type;
        this.size = size;

        this.mappedDeviceMemory = new AtomicReference<>(null);
    }

    @Override
    protected void destroy(long handle) {
        MappedDeviceMemory mappedMemory = mappedDeviceMemory.getAndSet(null);
        if (mappedMemory != null) {
            mappedMemory.destroy();
        }

        VK10.vkFreeMemory(device.getReference(), handle, null);
    }

    public Device getDevice() {
        return device;
    }

    public MemoryType getType() {
        return type;
    }

    public long getSize() {
        return size;
    }

    @Nullable
    public MappedDeviceMemory getMappedMemory() {
        return mappedDeviceMemory.get();
    }

    void map(MappedDeviceMemory mappedMemory) {
        mappedDeviceMemory.set(mappedMemory);
    }

    void unmap() {
        mappedDeviceMemory.getAndSet(null);
    }

    @Override
    public String toString() {
        return "DeviceMemory[" +
                "handle=" + handle +
                ", device=" + device +
                ", type=" + type +
                ", size=" + size +
                ", mappedDeviceMemory=" + mappedDeviceMemory +
                ']';
    }

    public String toSimpleString() {
        return "DeviceMemory[" +
                "handle=" + handle +
                ", device=" + device +
                ", type=" + type +
                ", size=" + size +
                ']';
    }
}
