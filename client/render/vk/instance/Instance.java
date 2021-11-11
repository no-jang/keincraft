package client.render.vk.instance;

import client.render.vk.debug.Debug;
import client.render.vk.debug.Validations;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.ByteBuffer;

import static client.render.vk.debug.Debug.vkCheck;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.vulkan.VK10.*;

public class Instance {
    private final VkInstance instance;
    private final long aDebugCallback;

    public Instance() {
        try (MemoryStack stack = stackPush()) {
            // Check for required validation layers and extensions
            PointerBuffer requiredLayers = Validations.checkValidationLayers(stack);
            PointerBuffer requiredExtensions = Extensions.checkExtensions(stack);

            // General application information
            ByteBuffer appName = stack.ASCII("minecraft-clone");
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

    public void destroy() {
        Debug.destroyDebugCallback(instance, aDebugCallback);

        vkDestroyInstance(instance, null);
    }

    public VkInstance getInstance() {
        return instance;
    }
}
