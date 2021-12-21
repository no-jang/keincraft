package engine.graphics.vulkan.swapchain;

import engine.graphics.vulkan.device.Device;
import engine.graphics.vulkan.sync.Fence;
import engine.graphics.vulkan.sync.Semaphore;
import engine.graphics.vulkan.util.function.VkFunction;
import engine.memory.MemoryContext;
import engine.util.pointer.DestroyablePointer;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.KHRSwapchain;
import org.lwjgl.vulkan.VK10;

import java.nio.IntBuffer;

public class Swapchain extends DestroyablePointer {
    private final Device device;
    private final int imageCount;

    public Swapchain(Device device, long handle, int imageCount) {
        super(handle);

        this.device = device;
        this.imageCount = imageCount;
    }

    @Override
    protected void destroy(long handle) {
        KHRSwapchain.vkDestroySwapchainKHR(device.getReference(), handle, null);
    }

    public int acquireNextImageIndex(@Nullable Semaphore semaphore, @Nullable Fence fence) {
        MemoryStack stack = MemoryContext.getStack();

        long semaphoreHandle;
        if (semaphore != null) {
            semaphoreHandle = semaphore.getHandle();
        } else {
            semaphoreHandle = VK10.VK_NULL_HANDLE;
        }

        long fenceHandle;
        if (fence != null) {
            fenceHandle = fence.getHandle();
        } else {
            fenceHandle = VK10.VK_NULL_HANDLE;
        }

        IntBuffer nextImageIndexBuffer = stack.mallocInt(1);
        VkFunction.execute(() -> KHRSwapchain.vkAcquireNextImageKHR(device.getReference(), handle, Long.MAX_VALUE,
                semaphoreHandle, fenceHandle, nextImageIndexBuffer));
        return nextImageIndexBuffer.get(0);
    }

    public int getImageCount() {
        return imageCount;
    }
}