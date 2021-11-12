package client.render.vk;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWVulkan;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static client.render.vk.Debug.vkCheck;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.vulkan.VK10.*;

public class Instance {
    // Default vulkan has very little to no error handling.
    // Even using a wrong enumeration value could cause a crash or undefined behavior
    // Validation layers can for example validate the input of a function to ensure that the values are sane
    private static final String[] requiredValidationLayerNames = new String[]{
            "VK_LAYER_KHRONOS_validation"
    };

    private static final List<String> requiredExtensionNames = new ArrayList<>();

    static {
        if (Debug.isDebug) {
            requiredExtensionNames.add(EXTDebugReport.VK_EXT_DEBUG_REPORT_EXTENSION_NAME);
        }
    }

    private final VkInstance instance;
    private final long aDebugCallback;

    public Instance() {
        try (MemoryStack stack = stackPush()) {
            // Check for required validation layers and extensions
            PointerBuffer requiredLayers = checkValidationLayers(stack);
            PointerBuffer requiredExtensions = checkExtensions(stack);

            // General application information
            ByteBuffer appName = stack.ASCII("keincraft");
            int appVersion = VK_MAKE_VERSION(1, 0, 0);

            VkApplicationInfo appInfo = VkApplicationInfo.malloc(stack)
                    .sType$Default()
                    .pNext(NULL)
                    .pApplicationName(appName)
                    .applicationVersion(appVersion)
                    .pEngineName(appName)
                    .engineVersion(appVersion)
                    .apiVersion(VK.getInstanceVersionSupported());

            // Instance creation information
            VkInstanceCreateInfo createInfo = VkInstanceCreateInfo.malloc(stack)
                    .sType$Default()
                    .pNext(NULL)
                    .flags(0)
                    .pApplicationInfo(appInfo)
                    .ppEnabledExtensionNames(requiredExtensions)
                    .ppEnabledLayerNames(requiredLayers);

            VkDebugReportCallbackCreateInfoEXT debugCallbackCreateInfo = Debug.createDebugCallback(stack, createInfo);

            PointerBuffer pInstance = stack.mallocPointer(1);
            vkCheck(vkCreateInstance(createInfo, null, pInstance), "Failed to create vulkan instance");
            instance = new VkInstance(pInstance.get(0), createInfo);

            aDebugCallback = Debug.setupDebugCallback(stack, instance, debugCallbackCreateInfo);
        }
    }

    private static PointerBuffer checkValidationLayers(MemoryStack stack) {
        if (!Debug.isDebug) {
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

        requiredLayers.position(0);
        return requiredLayers;
    }

    private static PointerBuffer checkExtensions(MemoryStack stack) {
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

    public void destroy() {
        Debug.destroyDebugCallback(instance, aDebugCallback);

        vkDestroyInstance(instance, null);
    }

    public VkInstance getInstance() {
        return instance;
    }
}
