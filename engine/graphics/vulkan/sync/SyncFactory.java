package engine.graphics.vulkan.sync;

import engine.graphics.vulkan.device.Device;
import engine.graphics.vulkan.sync.properties.FenceCreationFlag;
import engine.graphics.vulkan.util.function.VkFunction;
import engine.memory.MemoryContext;
import engine.util.enums.Maskable;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkFenceCreateInfo;
import org.lwjgl.vulkan.VkSemaphoreCreateInfo;

import java.nio.LongBuffer;
import java.util.List;

public class SyncFactory {
    public static Semaphore createSemaphore(Device device) {
        MemoryStack stack = MemoryContext.getStack();

        VkSemaphoreCreateInfo createInfo = VkSemaphoreCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0);

        LongBuffer semaphoreBuffer = stack.mallocLong(1);
        VkFunction.execute(() -> VK10.vkCreateSemaphore(device.getReference(), createInfo, null, semaphoreBuffer));

        return new Semaphore(device, semaphoreBuffer.get(0));
    }

    public static Fence createFence(Device device) {
        return createFence(device, null);
    }

    public static Fence createFence(Device device, @Nullable List<FenceCreationFlag> creationFlags) {
        MemoryStack stack = MemoryContext.getStack();

        int flags = 0;
        if (creationFlags != null) {
            flags = Maskable.toBitMask(creationFlags);
        }

        VkFenceCreateInfo createInfo = VkFenceCreateInfo.malloc(stack)
                .sType$Default()
                .flags(flags)
                .pNext(0);

        LongBuffer fenceBuffer = stack.mallocLong(1);
        VkFunction.execute(() -> VK10.vkCreateFence(device.getReference(), createInfo, null, fenceBuffer));

        return new Fence(device, fenceBuffer.get(0));
    }
}
