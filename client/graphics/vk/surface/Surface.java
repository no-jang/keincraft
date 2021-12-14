package client.graphics.vk.surface;

import client.graphics.vk.device.PhysicalDevice;
import client.graphics.vk.instance.VulkanInstance;
import client.graphics.vk.memory.MemoryContext;
import client.graphics.vk.models.function.CheckFunction;
import client.graphics.vk.models.function.EnumerateFunction;
import client.graphics.vk.models.pointers.DestroyablePointer;
import client.graphics.vk.surface.models.PresentMode;
import client.graphics.vk.surface.models.SurfaceCapabilities;
import client.graphics.vk.surface.models.SurfaceFormat;
import common.util.enums.HasValue;
import org.lwjgl.glfw.GLFWVulkan;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.KHRSurface;
import org.lwjgl.vulkan.VkSurfaceCapabilitiesKHR;
import org.lwjgl.vulkan.VkSurfaceFormatKHR;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;

public class Surface extends DestroyablePointer {
    private final long handle;
    private final VulkanInstance instance;
    private final SurfaceCapabilities capabilities;
    private final List<SurfaceFormat> formats;
    private final List<PresentMode> presentModes;

    public Surface(VulkanInstance instance, Window window, PhysicalDevice device) {
        MemoryStack stack = MemoryContext.getStack();

        this.instance = instance;

        LongBuffer pHandle = stack.mallocLong(1);
        CheckFunction.execute(() -> GLFWVulkan.glfwCreateWindowSurface(instance.getReference(), window.getHandle(), null, pHandle));
        handle = pHandle.get(0);

        VkSurfaceCapabilitiesKHR vkCapabilities = VkSurfaceCapabilitiesKHR.malloc(stack);
        CheckFunction.execute(() -> KHRSurface.vkGetPhysicalDeviceSurfaceCapabilitiesKHR(device.getReference(), handle, vkCapabilities));
        capabilities = new SurfaceCapabilities(vkCapabilities);

        VkSurfaceFormatKHR.Buffer pFormats = EnumerateFunction.execute(stack.mallocInt(1),
                (pCount, pBuffer) -> KHRSurface.vkGetPhysicalDeviceSurfaceFormatsKHR(device.getReference(), handle, pCount, pBuffer),
                count -> VkSurfaceFormatKHR.malloc(count, stack));

        formats = new ArrayList<>(pFormats.capacity());
        for (int i = 0; i < pFormats.capacity(); i++) {
            formats.add(new SurfaceFormat(pFormats.get(i)));
        }

        IntBuffer pPresentModes = EnumerateFunction.execute(stack.mallocInt(1),
                (pCount, pBuffer) -> KHRSurface.vkGetPhysicalDeviceSurfacePresentModesKHR(device.getReference(), handle, pCount, pBuffer),
                stack::mallocInt);

        presentModes = new ArrayList<>(pPresentModes.capacity());
        for (int i = 0; i < pPresentModes.capacity(); i++) {
            presentModes.add(HasValue.getByValue(pPresentModes.get(i), PresentMode.class));
        }
    }

    @Override
    protected void internalDestroy() {
        KHRSurface.vkDestroySurfaceKHR(instance.getReference(), handle, null);
    }

    @Override
    protected long getInternalHandle() {
        return handle;
    }

    public SurfaceCapabilities getCapabilities() {
        return capabilities;
    }

    public List<SurfaceFormat> getFormats() {
        return formats;
    }

    public List<PresentMode> getPresentModes() {
        return presentModes;
    }
}
