package client.render.vk.instance;

import client.render.vk.debug.VulkanDebug;
import client.render.vk.debug.VulkanValidation;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import static client.render.vk.debug.VulkanDebug.vkCheck;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.vulkan.VK10.*;

public class VulkanInstance {
    private final VkInstance instance;
    private final VkDebugReportCallbackEXT debugReportCallback;

    public VulkanInstance() {
        try (MemoryStack stack = stackPush()) {
            // Check if all requested validation layers are available
            PointerBuffer pRequiredValidationLayers = VulkanValidation.checkValidationLayerSupport(stack);

            // General application information
            VkApplicationInfo appInfo = VkApplicationInfo.mallocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_APPLICATION_INFO)
                    .pApplicationName(stack.ASCII("minecraft-clone"))
                    .applicationVersion(VK_MAKE_VERSION(1, 0, 0))
                    .pEngineName(stack.ASCII("minecraft-clone"))
                    .engineVersion(VK_MAKE_VERSION(1, 0, 0))
                    .apiVersion(VK_API_VERSION_1_0);

            VkInstanceCreateInfo createInfo = VkInstanceCreateInfo.mallocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO)
                    .pApplicationInfo(appInfo);

            // Vulkan extensions that are required for glfw to work
            PointerBuffer pRequiredExtensions = VulkanExtension.checkExtensionSupport(stack);
            pRequiredExtensions.flip();
            createInfo.ppEnabledExtensionNames(pRequiredExtensions);
            pRequiredExtensions.clear();

            // Enable validation layers
            if (VulkanValidation.validationLayersEnabled) {
                createInfo.ppEnabledLayerNames(pRequiredValidationLayers);
            } else {
                createInfo.ppEnabledLayerNames(); // Set no validation layers enabled
            }

            // Create debug message callback
            VkDebugReportCallbackCreateInfoEXT debugCreateInfo = VulkanDebug.createDebugCallback(stack, createInfo);

            // Creates vulkan instance
            PointerBuffer instancePointer = stack.pointers(1);
            vkCheck(vkCreateInstance(createInfo, null, instancePointer), "Could not initialize vulkan instance");
            instance = new VkInstance(instancePointer.get(), createInfo);

            // Setup debug message callback with instance
            debugReportCallback = VulkanDebug.setupDebugCallback(stack, instance, debugCreateInfo);
        }
    }

    public void destroy() {
        VulkanDebug.destroyDebugCallback(instance, debugReportCallback);

        vkDestroyInstance(instance, null);
    }
}
