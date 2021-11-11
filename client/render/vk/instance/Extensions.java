package client.render.vk.instance;

import client.render.vk.debug.Debug;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWVulkan;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.EXTDebugReport;
import org.lwjgl.vulkan.VkExtensionProperties;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static client.render.vk.debug.Debug.vkCheck;
import static org.lwjgl.vulkan.VK10.vkEnumerateInstanceExtensionProperties;

public final class Extensions {
    private static final List<String> requiredExtensionNames = new ArrayList<>();

    static {
        if (Debug.debugEnabled) {
            requiredExtensionNames.add(EXTDebugReport.VK_EXT_DEBUG_REPORT_EXTENSION_NAME);
        }
    }

    public static PointerBuffer checkExtensions(MemoryStack stack) {
        PointerBuffer pRequiredGlfwExtensions = GLFWVulkan.glfwGetRequiredInstanceExtensions();
        if (pRequiredGlfwExtensions == null) {
            throw new RuntimeException("Failed to find platform surface extensions");
        }

        PointerBuffer pRequiredExtensions = stack.mallocPointer(pRequiredGlfwExtensions.capacity() + requiredExtensionNames.size());

        // Add required glfw extensions
        for (int glfwExtensionIndex = 0; glfwExtensionIndex < pRequiredGlfwExtensions.capacity(); glfwExtensionIndex++) {
            pRequiredExtensions.put(pRequiredGlfwExtensions.get(glfwExtensionIndex));
        }

        // Enumerate available extension count
        IntBuffer pAvailableExtensionCount = stack.mallocInt(1);
        vkCheck(vkEnumerateInstanceExtensionProperties((String) null, pAvailableExtensionCount, null), "Failed to enumerate extension count");
        int availableExtensionCount = pAvailableExtensionCount.get(0);

        // Enumerate available extensions
        VkExtensionProperties.Buffer pAvailableExtensions = VkExtensionProperties.malloc(availableExtensionCount, stack);
        vkCheck(vkEnumerateInstanceExtensionProperties((String) null, pAvailableExtensionCount, pAvailableExtensions), "Failed to enumerate extensions");

        // Check if all required extensions are available
        for (String requiredExtensionName : requiredExtensionNames) {
            boolean found = false;

            for (int availableExtensionIndex = 0; availableExtensionIndex < availableExtensionCount; availableExtensionIndex++) {
                if (requiredExtensionName.equals(pAvailableExtensions.get(availableExtensionIndex).extensionNameString())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new RuntimeException("Failed to find required extension: " + requiredExtensionName);
            }

            pRequiredExtensions.put(stack.ASCII(requiredExtensionName));
        }

        pRequiredExtensions.flip();
        return pRequiredExtensions;
    }
}
