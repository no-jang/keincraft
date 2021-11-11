package client.render.vk.debug;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkLayerProperties;

import java.nio.IntBuffer;

import static client.render.vk.debug.VulkanDebug.vkCheck;
import static org.lwjgl.vulkan.VK10.vkEnumerateInstanceLayerProperties;

public final class VulkanValidation {
    // Default vulkan has very little to no error handling.
    // Even using a wrong enumeration value could cause a crash or undefined behavior
    // Validation layers can for example validate the input of a function to ensure that the values are sane
    private static final String[] requiredValidationLayerNames = new String[]{
            "VK_LAYER_KHRONOS_validation"
    };

    public static PointerBuffer checkValidationLayers(MemoryStack stack) {
        if (!VulkanDebug.debugEnabled) {
            return null;
        }

        // Enumerate available validation layer count
        IntBuffer pAvailableLayerCount = stack.mallocInt(1);
        vkCheck(vkEnumerateInstanceLayerProperties(pAvailableLayerCount, null), "Failed to enumerate validation layer count");
        int availableLayerCount = pAvailableLayerCount.get(0);

        // Enumerate available validation layers
        VkLayerProperties.Buffer pAvailableLayers = VkLayerProperties.malloc(availableLayerCount, stack);
        vkCheck(vkEnumerateInstanceLayerProperties(pAvailableLayerCount, pAvailableLayers), "Failed to enumerate validation layers");

        PointerBuffer requiredLayers = stack.mallocPointer(requiredValidationLayerNames.length);

        // Check if all required validation layers are available
        for (String requiredValidationLayerName : requiredValidationLayerNames) {
            boolean found = false;

            for (int availableLayerIndex = 0; availableLayerIndex < availableLayerCount; availableLayerIndex++) {
                if (requiredValidationLayerName.equals(pAvailableLayers.get(availableLayerIndex).layerNameString())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new RuntimeException("Failed to find required validation layer: " + requiredValidationLayerName);
            }

            requiredLayers.put(stack.ASCII(requiredValidationLayerName));
        }

        return requiredLayers;
    }
}
