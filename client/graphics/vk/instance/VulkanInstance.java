package client.graphics.vk.instance;

import client.graphics.vk.instance.models.ApplicationInfo;
import client.graphics.vk.instance.models.DebugInfo;
import client.graphics.vk.instance.models.InstanceInfo;
import client.graphics.vk.instance.models.Version;
import client.graphics.vk.models.Maskable;
import client.graphics.vk.models.function.CheckIntFunction;
import client.graphics.vk.models.function.CheckLongFunction;
import client.graphics.vk.models.function.CheckPointerFunction;
import client.graphics.vk.models.function.EnumerateFunction;
import client.graphics.vk.models.pointers.DestroyableReferencePointer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.EXTDebugReport;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VK11;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkDebugReportCallbackCreateInfoEXT;
import org.lwjgl.vulkan.VkDebugReportCallbackEXT;
import org.lwjgl.vulkan.VkExtensionProperties;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;
import org.lwjgl.vulkan.VkLayerProperties;
import org.tinylog.Logger;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class VulkanInstance extends DestroyableReferencePointer<VkInstance> {
    private final VkInstance handle;
    private final long debugHandle;

    public VulkanInstance(ApplicationInfo applicationInfo, InstanceInfo instanceInfo, DebugInfo debugInfo) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            Version availableApiVersion = Version.fromVulkanVersion(CheckIntFunction.execute(stack.mallocInt(1),
                    VK11::vkEnumerateInstanceVersion));

            if (availableApiVersion.compareTo(applicationInfo.getApiVersion()) < 0) {
                throw new RuntimeException("Available api version " + availableApiVersion + " is lower than required api version " + applicationInfo.getApiVersion());
            }

            Logger.debug("API Version: {}", availableApiVersion);

            VkExtensionProperties.Buffer pAvailableExtensions = EnumerateFunction.execute(stack.mallocInt(1),
                    (pCount, pBuffer) -> VK10.vkEnumerateInstanceExtensionProperties((String) null, pCount, pBuffer),
                    (count) -> VkExtensionProperties.malloc(count, stack));

            List<String> requiredExtensions = new ArrayList<>(instanceInfo.getRequiredExtensions());
            List<String> optionalExtensions = new ArrayList<>(instanceInfo.getOptionalExtensions());
            int requiredExtensionCount = requiredExtensions.size() + optionalExtensions.size();

            Logger.debug("Found {} extensions", pAvailableExtensions.capacity());

            PointerBuffer pExtensions = stack.mallocPointer(requiredExtensionCount);
            for (int i = 0; i < pAvailableExtensions.capacity(); i++) {
                VkExtensionProperties extension = pAvailableExtensions.get(i);
                String extensionName = extension.extensionNameString();

                Logger.trace(extensionName);

                int requiredIndex = requiredExtensions.indexOf(extensionName);
                if (requiredIndex != -1) {
                    pExtensions.put(extension.extensionName());
                    instanceInfo.getEnabledExtensions().add(extensionName);
                    requiredExtensions.remove(requiredIndex);
                    continue;
                }

                int optionalIndex = optionalExtensions.indexOf(extensionName);
                if (optionalIndex != -1) {
                    pExtensions.put(extension.extensionName());
                    instanceInfo.getEnabledExtensions().add(extensionName);
                    optionalExtensions.remove(optionalIndex);
                }
            }

            if (!requiredExtensions.isEmpty()) {
                throw new RuntimeException("Failed to find required vulkan instance extensions: " + String.join(", ", requiredExtensions));
            }

            if (!optionalExtensions.isEmpty()) {
                Logger.debug("Could not find optional vulkan instance extensions: {}", optionalExtensions);
            }

            pExtensions.flip();

            VkLayerProperties.Buffer pAvailableLayers = EnumerateFunction.execute(stack.mallocInt(1),
                    VK10::vkEnumerateInstanceLayerProperties,
                    count -> VkLayerProperties.malloc(count, stack));

            List<String> requiredLayers = new ArrayList<>(instanceInfo.getRequiredLayers());
            List<String> optionalLayers = new ArrayList<>(instanceInfo.getOptionalLayers());
            int requiredLayerCount = requiredLayers.size() + optionalLayers.size();

            Logger.debug("Found {} layers", pAvailableLayers.capacity());

            PointerBuffer pLayers = stack.mallocPointer(requiredLayerCount);
            for (int i = 0; i < pAvailableLayers.capacity(); i++) {
                VkLayerProperties layer = pAvailableLayers.get(i);
                String layerName = layer.layerNameString();

                Logger.trace(layerName);

                int requiredIndex = requiredLayers.indexOf(layerName);
                if (requiredIndex != -1) {
                    pLayers.put(layer.layerName());
                    instanceInfo.getEnabledLayers().add(layerName);
                    requiredLayers.remove(requiredIndex);
                    continue;
                }

                int optionalIndex = optionalLayers.indexOf(layerName);
                if (optionalIndex != -1) {
                    pLayers.put(layer.layerName());
                    instanceInfo.getEnabledLayers().add(layerName);
                    optionalLayers.remove(optionalIndex);
                }
            }

            ByteBuffer pApplicationName = stack.ASCII(applicationInfo.getApplicationName());
            ByteBuffer pEngineName = stack.ASCII(applicationInfo.getEngineName());

            VkApplicationInfo appInfo = VkApplicationInfo.malloc(stack)
                    .sType$Default()
                    .pNext(0)
                    .pApplicationName(pApplicationName)
                    .pEngineName(pEngineName)
                    .applicationVersion(applicationInfo.getApplicationVersion().toVulkanVersion())
                    .engineVersion(applicationInfo.getEngineVersion().toVulkanVersion())
                    .apiVersion(applicationInfo.getApiVersion().toVulkanVersion());

            VkInstanceCreateInfo createInfo = VkInstanceCreateInfo.malloc(stack)
                    .sType$Default()
                    .flags(0)
                    .pNext(0)
                    .pApplicationInfo(appInfo)
                    .ppEnabledExtensionNames(pExtensions)
                    .ppEnabledLayerNames(pLayers);

            final VkDebugReportCallbackCreateInfoEXT debugCreateInfo;
            if (!debugInfo.getSeverities().isEmpty()) {
                debugCreateInfo = VkDebugReportCallbackCreateInfoEXT.malloc(stack)
                        .sType$Default()
                        .flags(0)
                        .pNext(0)
                        .flags(Maskable.toBitMask(debugInfo.getSeverities()))
                        .pfnCallback(VkDebugReportCallbackEXT.create(new DebugLogger()))
                        .pUserData(0);

                createInfo.pNext(debugCreateInfo);
            } else {
                debugCreateInfo = null;
            }

            handle = new VkInstance(
                    CheckPointerFunction.execute(
                            stack.mallocPointer(1),
                            pHandle -> VK10.vkCreateInstance(createInfo, null, pHandle)),
                    createInfo);

            if (debugCreateInfo != null) {
                debugHandle = CheckLongFunction.execute(
                        stack.mallocLong(1),
                        pHandle -> EXTDebugReport.vkCreateDebugReportCallbackEXT(handle, debugCreateInfo, null, pHandle));
            } else {
                debugHandle = -1L;
            }
        }
    }

    @Override
    protected void internalDestroy() {
        if (debugHandle != -1L) {
            EXTDebugReport.vkDestroyDebugReportCallbackEXT(handle, debugHandle, null);
        }

        VK10.vkDestroyInstance(handle, null);
    }

    @Override
    protected long getInternalHandle() {
        return handle.address();
    }

    @Override
    protected VkInstance getInternalReference() {
        return handle;
    }
}
