package client.render.vk.debug;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkLayerProperties;

import java.nio.IntBuffer;
import java.util.List;

import static org.lwjgl.vulkan.VK10.vkEnumerateInstanceLayerProperties;

public final class VulkanValidation {
    // Default vulkan has very little to no error handling.
    // Even using a wrong enumeration value could cause a crash or undefined behavior
    // Validation layers can for example validate the input of a function to ensure that the values are sane
    public static final List<String> validationsLayers = List.of(
            "VK_LAYER_KHRONOS_validation"
    );

    // Enabling validation layers extends the overhead
    public static final boolean validationLayersEnabled = true;

    public static PointerBuffer checkValidationLayerSupport(MemoryStack stack) {
        if (!validationLayersEnabled) return null;

        // Get supported validation layer count
        IntBuffer pSupportedValidationLayerCount = stack.mallocInt(1);
        vkEnumerateInstanceLayerProperties(pSupportedValidationLayerCount, null);
        int supportedValidationLayerCount = pSupportedValidationLayerCount.get();

        PointerBuffer pRequiredValidationLayers = stack.mallocPointer(validationsLayers.size());
        pSupportedValidationLayerCount.position(0);

        // Get supported validation layer
        VkLayerProperties.Buffer pSupportedValidationLayers = VkLayerProperties.mallocStack(supportedValidationLayerCount, stack);
        vkEnumerateInstanceLayerProperties(pSupportedValidationLayerCount, pSupportedValidationLayers);

        // Check if all required validation layers are also available
        for (String requiredLayer : validationsLayers) {
            boolean found = false;
            for (int availableLayerIndex = 0; availableLayerIndex < supportedValidationLayerCount; availableLayerIndex++) {
                VkLayerProperties availableLayer = pSupportedValidationLayers.get(availableLayerIndex);
                if (availableLayer.layerNameString().equals(requiredLayer)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new RuntimeException("Validation layer " + requiredLayer + " requested but not available");
            }

            pRequiredValidationLayers.put(stack.ASCII(requiredLayer));
        }

        pRequiredValidationLayers.position(0);
        return pRequiredValidationLayers;
    }
}
