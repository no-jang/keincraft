package client.render.vk.instance;

import client.render.vk.debug.VulkanDebug;
import client.render.vk.debug.VulkanValidation;
import client.util.BufferUtil;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.IntBuffer;

import static client.render.vk.debug.VulkanDebug.vkCheck;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.vulkan.VK10.*;

public class VulkanInstance {
    private final VkInstance instance;
    private final VkDebugReportCallbackEXT debugReportCallback;

    public VulkanInstance() {
        try (MemoryStack stack = stackPush()) {
            // Check if all requested validation layers and extensions are available
            PointerBuffer pRequiredValidationLayers = VulkanValidation.checkValidationLayers(stack);
            PointerBuffer pRequiredExtensions = VulkanExtension.checkExtensions(stack);

            BufferUtil.printString(pRequiredValidationLayers);
            BufferUtil.printString(pRequiredExtensions);

            // General application information
            VkApplicationInfo appInfo = VkApplicationInfo.mallocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_APPLICATION_INFO)
                    .pApplicationName(stack.ASCII("minecraft-clone"))
                    .applicationVersion(VK_MAKE_VERSION(1, 0, 0))
                    .pEngineName(stack.ASCII("minecraft-clone"))
                    .engineVersion(VK_MAKE_VERSION(1, 0, 0))
                    .apiVersion(VK_API_VERSION_1_0);

            pRequiredExtensions.flip();

            VkInstanceCreateInfo createInfo = VkInstanceCreateInfo.mallocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO)
                    .pApplicationInfo(appInfo)
                    .ppEnabledExtensionNames(pRequiredExtensions)
                    .ppEnabledLayerNames(pRequiredValidationLayers);

            pRequiredExtensions.clear();

            // Create debug message callback
            VkDebugReportCallbackCreateInfoEXT debugCreateInfo = VulkanDebug.createDebugCallback(stack, createInfo);

            // Creates vulkan instance
            PointerBuffer instancePointer = stack.mallocPointer(1);
            vkCheck(vkCreateInstance(createInfo, null, instancePointer), "Could not initialize vulkan instance");
            instance = new VkInstance(instancePointer.get(0), createInfo);

            IntBuffer pGpuCount = stack.mallocInt(1);

            vkCheck(vkEnumeratePhysicalDevices(instance, pGpuCount, null), "Failed to acquire gpu count");

            VkPhysicalDevice gpu;
            if (pGpuCount.get(0) > 0) {
                PointerBuffer physical_devices = stack.mallocPointer(pGpuCount.get(0));
                vkCheck(vkEnumeratePhysicalDevices(instance, pGpuCount, physical_devices), "Failed to acquire gpu");

                /* For tri demo we just grab the first physical device */
                gpu = new VkPhysicalDevice(physical_devices.get(0), instance);
            } else {
                throw new IllegalStateException("vkEnumeratePhysicalDevices reported zero accessible devices.");
            }

            IntBuffer pDeviceExtensionCount = stack.mallocInt(1);

            /* Look for device extensions */
            boolean swapchainExtFound = false;
            vkCheck(vkEnumerateDeviceExtensionProperties(gpu, (String) null, pDeviceExtensionCount, null), "Failed to acquire device extension count");

            if (pDeviceExtensionCount.get(0) > 0) {
                VkExtensionProperties.Buffer device_extensions = VkExtensionProperties.mallocStack(pDeviceExtensionCount.get(0), stack);
                vkCheck(vkEnumerateDeviceExtensionProperties(gpu, (String) null, pDeviceExtensionCount, device_extensions), "\"Failed to acquire device extension count\"");

                for (int i = 0; i < pDeviceExtensionCount.get(0); i++) {
                    device_extensions.position(i);
                    if (KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME.equals(device_extensions.extensionNameString())) {
                        swapchainExtFound = true;
                        //extension_names.put(KHR_swapchain);
                    }
                }
            }

            if (!swapchainExtFound) {
                throw new IllegalStateException("vkEnumerateDeviceExtensionProperties failed to find the " + KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME + " extension.");
            }

            // Setup debug message callback with instance
            debugReportCallback = VulkanDebug.setupDebugCallback(stack, instance, debugCreateInfo);
        }
    }

    public void destroy() {
        VulkanDebug.destroyDebugCallback(instance, debugReportCallback);

        vkDestroyInstance(instance, null);
    }
}
