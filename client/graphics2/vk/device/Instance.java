package client.graphics2.vk.device;

import client.ClientConstants;
import client.graphics2.vk.util.Check;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWVulkan;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.EXTDebugReport;
import org.lwjgl.vulkan.VK;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkDebugReportCallbackCreateInfoEXT;
import org.lwjgl.vulkan.VkDebugReportCallbackEXT;
import org.lwjgl.vulkan.VkExtensionProperties;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;
import org.lwjgl.vulkan.VkLayerProperties;
import org.tinylog.Logger;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.vulkan.EXTDebugReport.VK_DEBUG_REPORT_DEBUG_BIT_EXT;
import static org.lwjgl.vulkan.EXTDebugReport.VK_DEBUG_REPORT_ERROR_BIT_EXT;
import static org.lwjgl.vulkan.EXTDebugReport.VK_DEBUG_REPORT_INFORMATION_BIT_EXT;
import static org.lwjgl.vulkan.EXTDebugReport.VK_DEBUG_REPORT_PERFORMANCE_WARNING_BIT_EXT;
import static org.lwjgl.vulkan.EXTDebugReport.VK_DEBUG_REPORT_WARNING_BIT_EXT;
import static org.lwjgl.vulkan.EXTDebugReport.vkCreateDebugReportCallbackEXT;
import static org.lwjgl.vulkan.EXTDebugReport.vkDestroyDebugReportCallbackEXT;
import static org.lwjgl.vulkan.VK10.VK_FALSE;
import static org.lwjgl.vulkan.VK10.vkCreateInstance;
import static org.lwjgl.vulkan.VK10.vkDestroyInstance;

/**
 * Wrapper around a vulkan instance. Because there is no global state in vulkan this information needs to be stored in an
 * instance. In addition, the vulkan library initializes the vulkan library and allows the application to pass information
 * about itself to the graphics driver.
 */
public class Instance {
    /**
     * List of validation layers that will be enabled if debug is set to true. These help to find error in vulkan calls
     * which usually aren't validated
     */
    private static final String[] requiredValidationLayers = new String[]{
            "VK_LAYER_KHRONOS_validation"
    };

    /**
     * List of vulkan extensions that need to be present or the game won't be able to run
     */
    private static final List<String> requiredExtensions = new ArrayList<>();

    static {
        // If debug mode is enabled activate validation layer debug output
        if (ClientConstants.IS_DEBUG) {
            requiredExtensions.add(EXTDebugReport.VK_EXT_DEBUG_REPORT_EXTENSION_NAME);
        }
    }

    private final VkInstance handle;
    private final long debugCallback;

    /**
     * Creates a new instance and loads all required extensions.
     * Initializes the validation layers and debug output if debug mode is enabled.
     *
     * @param stack memory stack
     */
    public Instance(MemoryStack stack) {
        // Check for required validation layers and extensions
        PointerBuffer requiredLayers = checkValidationLayers(stack);
        PointerBuffer requiredExtensions = checkExtensions(stack);

        // Instance create information
        ByteBuffer appName = stack.ASCII("keincraft");
        int appVersion = VK10.VK_MAKE_VERSION(1, 0, 0);

        VkApplicationInfo appInfo = VkApplicationInfo.malloc(stack)
                .sType$Default()
                .pNext(0)
                .pApplicationName(appName)
                .pEngineName(appName)
                .applicationVersion(appVersion)
                .engineVersion(appVersion)
                .apiVersion(VK.getInstanceVersionSupported());

        VkInstanceCreateInfo createInfo = VkInstanceCreateInfo.malloc(stack)
                .sType$Default()
                .pNext(0)
                .flags(0)
                .pApplicationInfo(appInfo)
                .ppEnabledLayerNames(requiredLayers)
                .ppEnabledExtensionNames(requiredExtensions);

        // If debug mode setup logging callback
        VkDebugReportCallbackCreateInfoEXT debugCallbackCreateInfo = createDebugCallback(stack, createInfo);

        PointerBuffer pInstance = stack.mallocPointer(1);
        Check.vkCheck(vkCreateInstance(createInfo, null, pInstance), "Failed to create vulkan instance");
        handle = new VkInstance(pInstance.get(0), createInfo);

        // Setup final debug logging callback
        if (debugCallbackCreateInfo != null) {
            LongBuffer pDebugReportCallback = stack.mallocLong(1);
            Check.vkCheck(vkCreateDebugReportCallbackEXT(handle, debugCallbackCreateInfo, null, pDebugReportCallback), "Failed to create debug report callback");
            debugCallback = pDebugReportCallback.get(0);
        } else {
            debugCallback = 0;
        }
    }

    /**
     * Creates debug report callback for logging validation layer messages
     *
     * @param stack              memory stack
     * @param instanceCreateInfo vulkan instance create info
     * @return debug report callback
     */
    private static VkDebugReportCallbackCreateInfoEXT createDebugCallback(MemoryStack stack, VkInstanceCreateInfo instanceCreateInfo) {
        // Only if debug mode is enabled
        if (!ClientConstants.IS_DEBUG) {
            return null;
        }

        VkDebugReportCallbackEXT debugCallbackFunction = VkDebugReportCallbackEXT.create(
                (flags, objectType, object, location, messageCode, pLayerPrefix, pMessage, pUserData) -> {
                    String layerPrefix = MemoryUtil.memASCII(pLayerPrefix);
                    String message = VkDebugReportCallbackEXT.getString(pMessage);

                    if ((flags & VK_DEBUG_REPORT_INFORMATION_BIT_EXT) != 0) {
                        Logger.info("validation layer {}: {} {}", layerPrefix, messageCode, message);
                    } else if ((flags & VK_DEBUG_REPORT_WARNING_BIT_EXT) != 0) {
                        Logger.warn("validation layer {}: {} {}", layerPrefix, messageCode, message);
                    } else if ((flags & VK_DEBUG_REPORT_PERFORMANCE_WARNING_BIT_EXT) != 0) {
                        Logger.warn("validation layer performance {}: {} {}", layerPrefix, messageCode, message);
                    } else if ((flags & VK_DEBUG_REPORT_ERROR_BIT_EXT) != 0) {
                        Logger.error("validation layer {}: {} {}", layerPrefix, messageCode, message);
                    } else if ((flags & VK_DEBUG_REPORT_DEBUG_BIT_EXT) != 0) {
                        Logger.debug("validation layer {}: {} {}", layerPrefix, messageCode, message);
                    } else {
                        Logger.info("validation layer unknown {}: {} {}", layerPrefix, messageCode, message);
                    }

                    return VK_FALSE;
                });

        VkDebugReportCallbackCreateInfoEXT createInfo = VkDebugReportCallbackCreateInfoEXT.malloc(stack)
                .sType$Default()
                .pNext(0)
                .flags(VK_DEBUG_REPORT_ERROR_BIT_EXT | VK_DEBUG_REPORT_WARNING_BIT_EXT | VK_DEBUG_REPORT_PERFORMANCE_WARNING_BIT_EXT)
                .pfnCallback(debugCallbackFunction)
                .pUserData(0);

        // Add callback to instance create info
        instanceCreateInfo.pNext(createInfo.address());
        return createInfo;
    }

    /**
     * If debug mode is enabled gathers all required validation layers and checks if there are present
     *
     * @param stack memory stack
     * @return a buffer with available and required validation layer names
     */
    private static PointerBuffer checkValidationLayers(MemoryStack stack) {
        // Don't use validation layers if no debug mode
        if (!ClientConstants.IS_DEBUG) {
            Logger.debug("Debug mode is disable. No validation layers");
            return null;
        }

        // Get available validation layer count
        IntBuffer pAvailableLayerCount = stack.mallocInt(1);
        Check.vkCheck(VK10.vkEnumerateInstanceLayerProperties(pAvailableLayerCount, null), "Failed to get validation layer count");
        int availableLayerCount = pAvailableLayerCount.get(0);

        // Get available validation layers
        VkLayerProperties.Buffer pAvailableLayers = VkLayerProperties.malloc(availableLayerCount, stack);
        Check.vkCheck(VK10.vkEnumerateInstanceLayerProperties(pAvailableLayerCount, pAvailableLayers), "Failed to get validation layers");

        if (Logger.isTraceEnabled()) {
            for (int i = 0; i < availableLayerCount; i++) {
                Logger.trace(pAvailableLayers.get(i).layerNameString());
            }
        }
        Logger.debug("Found {} validation layers", availableLayerCount);

        PointerBuffer requiredLayers = stack.mallocPointer(requiredValidationLayers.length);

        // Check if all required validation layers are available
        for (String requiredLayer : requiredValidationLayers) {
            boolean found = false;

            for (int i = 0; i < availableLayerCount; i++) {
                if (requiredLayer.equals(pAvailableLayers.get(i).layerNameString())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                Logger.error("Failed to find required validation layer {}", requiredLayer);
            }

            requiredLayers.put(stack.ASCII(requiredLayer));
        }

        // Set buffer pos to 0 so vulkan can read from the beginning
        requiredLayers.position(0);
        return requiredLayers;
    }

    /**
     * Gathers the glfw and required extensions and checks if there are present
     *
     * @param stack memory stack
     * @return a buffer with available and required extension names
     */
    private static PointerBuffer checkExtensions(MemoryStack stack) {
        // GLFW needs extra extensions being enabled
        PointerBuffer pRequiredGlfwExtensions = GLFWVulkan.glfwGetRequiredInstanceExtensions();
        if (pRequiredGlfwExtensions == null) {
            throw new RuntimeException("Failed to find platform surface extensions");
        }

        PointerBuffer pRequiredExtensions = stack.mallocPointer(pRequiredGlfwExtensions.capacity() + requiredExtensions.size());

        // Add GLFW extensions
        for (int i = 0; i < pRequiredGlfwExtensions.capacity(); i++) {
            pRequiredExtensions.put(pRequiredGlfwExtensions.get(i));
        }

        // Get available extension count
        IntBuffer pAvailableExtensionCount = stack.mallocInt(1);
        Check.vkCheck(VK10.vkEnumerateInstanceExtensionProperties((String) null, pAvailableExtensionCount, null), "Failed to get instance extension count");
        int availableExtensionCount = pAvailableExtensionCount.get(0);

        // Get available extensions
        VkExtensionProperties.Buffer pAvailableExtensions = VkExtensionProperties.malloc(availableExtensionCount, stack);
        Check.vkCheck(VK10.vkEnumerateInstanceExtensionProperties((String) null, pAvailableExtensionCount, pAvailableExtensions), "Failed to get instance extensions");

        if (Logger.isTraceEnabled()) {
            for (int i = 0; i < availableExtensionCount; i++) {
                Logger.trace(pAvailableExtensions.get(i).extensionNameString());
            }
        }
        Logger.debug("Found {} instance extensions", availableExtensionCount);

        // Check if all required extensions are available
        for (String requiredExtension : requiredExtensions) {
            boolean found = false;

            for (int i = 0; i < availableExtensionCount; i++) {
                if (requiredExtension.equals(pAvailableExtensions.get(i).extensionNameString())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new RuntimeException("Failed to find required extension " + requiredExtension);
            }

            pRequiredExtensions.put(stack.ASCII(requiredExtension));
        }

        pRequiredExtensions.flip(); // Don't know why this is needed, but it is or SEGMENTATION_FAULT
        return pRequiredExtensions;
    }

    /**
     * Destroys the instance
     */
    public void destroy() {
        // If debug mode destroy debug callback
        if (ClientConstants.IS_DEBUG) {
            vkDestroyDebugReportCallbackEXT(handle, debugCallback, null);
        }

        vkDestroyInstance(handle, null);
    }

    /**
     * @return vulkans internal handle of the instance
     */
    public VkInstance getHandle() {
        return handle;
    }
}
