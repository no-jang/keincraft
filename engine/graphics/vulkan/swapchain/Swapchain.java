package engine.graphics.vulkan.swapchain;

import engine.graphics.vulkan.device.Device;
import engine.graphics.vulkan.properties.Extent2D;
import engine.graphics.vulkan.queue.QueueFamily;
import engine.graphics.vulkan.surface.properties.PresentMode;
import engine.graphics.vulkan.surface.properties.SurfaceFormat;
import engine.graphics.vulkan.surface.properties.SurfaceTransform;
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
    private final SurfaceFormat format;
    private final Extent2D imageExtent;
    private final SurfaceTransform surfaceTransform;
    private final PresentMode presentMode;
    private final int minImageCount;
    private final int imageCount;
    @Nullable
    private final QueueFamily graphicsFamily;
    @Nullable
    private final QueueFamily presentFamily;

    public Swapchain(long handle,
                     Device device,
                     SurfaceFormat format,
                     Extent2D imageExtent,
                     SurfaceTransform surfaceTransform,
                     PresentMode presentMode,
                     int minImageCount,
                     int imageCount,
                     @Nullable QueueFamily graphicsFamily,
                     @Nullable QueueFamily presentFamily) {
        super(handle);
        this.device = device;
        this.format = format;
        this.imageExtent = imageExtent;
        this.surfaceTransform = surfaceTransform;
        this.presentMode = presentMode;
        this.minImageCount = minImageCount;
        this.imageCount = imageCount;
        this.graphicsFamily = graphicsFamily;
        this.presentFamily = presentFamily;
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

    public Device getDevice() {
        return device;
    }

    public SurfaceFormat getFormat() {
        return format;
    }

    public Extent2D getImageExtent() {
        return imageExtent;
    }

    public SurfaceTransform getSurfaceTransform() {
        return surfaceTransform;
    }

    public PresentMode getPresentMode() {
        return presentMode;
    }

    public int getMinImageCount() {
        return minImageCount;
    }

    public int getImageCount() {
        return imageCount;
    }

    @Nullable
    public QueueFamily getGraphicsFamily() {
        return graphicsFamily;
    }

    @Nullable
    public QueueFamily getPresentFamily() {
        return presentFamily;
    }
}