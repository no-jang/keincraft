package engine.graphics.vulkan.surface;

import engine.graphics.vulkan.device.PhysicalDevice;
import engine.graphics.vulkan.instance.Instance;
import engine.graphics.vulkan.surface.properties.PresentMode;
import engine.graphics.vulkan.surface.properties.SurfaceCapabilities;
import engine.graphics.vulkan.surface.properties.SurfaceFormat;
import engine.graphics.vulkan.util.function.VkFunction;
import engine.memory.MemoryContext;
import engine.memory.util.EnumBuffers;
import engine.window.Window;
import org.lwjgl.glfw.GLFWVulkan;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.KHRSurface;
import org.lwjgl.vulkan.VkSurfaceCapabilitiesKHR;
import org.lwjgl.vulkan.VkSurfaceFormatKHR;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;

public class SurfaceFactory {
    public static Surface createSurface(Instance instance, PhysicalDevice physicalDevice, Window window) {
        MemoryStack stack = MemoryContext.getStack();

        LongBuffer handleBuffer = stack.mallocLong(1);
        VkFunction.execute(() -> GLFWVulkan.glfwCreateWindowSurface(instance.getReference(), window.getHandle(), null, handleBuffer));
        long handle = handleBuffer.get(0);

        VkSurfaceCapabilitiesKHR vkCapabilities = VkSurfaceCapabilitiesKHR.malloc(stack);
        VkFunction.execute(() -> KHRSurface.vkGetPhysicalDeviceSurfaceCapabilitiesKHR(physicalDevice.getReference(), handle, vkCapabilities));
        SurfaceCapabilities capabilities = new SurfaceCapabilities(vkCapabilities);

        IntBuffer formatCountBuffer = stack.mallocInt(1);
        VkFunction.execute(() -> KHRSurface.vkGetPhysicalDeviceSurfaceFormatsKHR(physicalDevice.getReference(), handle, formatCountBuffer, null));
        int formatCount = formatCountBuffer.get(0);

        VkSurfaceFormatKHR.Buffer formatBuffer = VkSurfaceFormatKHR.malloc(formatCount, stack);
        VkFunction.execute(() -> KHRSurface.vkGetPhysicalDeviceSurfaceFormatsKHR(physicalDevice.getReference(), handle, formatCountBuffer, formatBuffer));

        List<SurfaceFormat> formats = new ArrayList<>(formatCount);
        for (int i = 0; i < formatCount; i++) {
            formats.add(new SurfaceFormat(formatBuffer.get(i)));
        }

        IntBuffer presentModeCountBuffer = stack.mallocInt(1);
        VkFunction.execute(() -> KHRSurface.vkGetPhysicalDeviceSurfacePresentModesKHR(physicalDevice.getReference(), handle, presentModeCountBuffer, null));
        int presentModeCount = presentModeCountBuffer.get(0);

        IntBuffer presentModeBuffer = stack.mallocInt(presentModeCount);
        VkFunction.execute(() -> KHRSurface.vkGetPhysicalDeviceSurfacePresentModesKHR(physicalDevice.getReference(), handle, presentModeBuffer, null));
        List<PresentMode> presentModes = EnumBuffers.ofInt(presentModeBuffer, PresentMode.class);

        return new Surface(handle, instance, window, capabilities, formats, presentModes);
    }
}
