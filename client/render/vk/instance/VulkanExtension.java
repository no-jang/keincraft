package client.render.vk.instance;

import client.render.vk.debug.VulkanValidation;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWVulkan;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.EXTDebugReport;
import org.lwjgl.vulkan.VkExtensionProperties;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.vulkan.VK10.vkEnumerateInstanceExtensionProperties;

public final class VulkanExtension {
    public static final List<String> extensions = new ArrayList<>();

    static {
        if (VulkanValidation.validationLayersEnabled) {
            extensions.add(EXTDebugReport.VK_EXT_DEBUG_REPORT_EXTENSION_NAME);
        }
    }

    public static PointerBuffer checkExtensionSupport(MemoryStack stack) {
        // Gather required glfw extensions
        PointerBuffer pGlfwExtensions = GLFWVulkan.glfwGetRequiredInstanceExtensions();

        PointerBuffer pRequiredExtensions = stack.mallocPointer(pGlfwExtensions.capacity() + extensions.size());
        for (int i = 0; i < pGlfwExtensions.capacity(); i++) {
            pRequiredExtensions.put(pGlfwExtensions.get(i));
        }

        IntBuffer pSupportedExtensionsCount = stack.mallocInt(1);
        vkEnumerateInstanceExtensionProperties((String) null, pSupportedExtensionsCount, null);
        int supportedExtensionCount = pSupportedExtensionsCount.get();

        pSupportedExtensionsCount.flip();

        VkExtensionProperties.Buffer pSupportedExtensions = VkExtensionProperties.mallocStack(supportedExtensionCount, stack);
        vkEnumerateInstanceExtensionProperties((String) null, pSupportedExtensionsCount, pSupportedExtensions);

        for (String requiredExtension : extensions) {
            boolean found = false;

            for (int availableExtensionIndex = 0; availableExtensionIndex < pSupportedExtensions.capacity(); availableExtensionIndex++) {
                VkExtensionProperties availableExtension = pSupportedExtensions.get(availableExtensionIndex);
                if (availableExtension.extensionNameString().equals(requiredExtension)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new RuntimeException("Extension " + requiredExtension + " requested but not available");
            }

            pRequiredExtensions.put(stack.ASCII(requiredExtension));
        }

        pRequiredExtensions.position(0);

        return pRequiredExtensions;
    }
}
