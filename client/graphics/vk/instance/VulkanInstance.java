package client.graphics.vk.instance;

import client.graphics.vk.device.PhysicalDevice;
import client.graphics.vk.instance.properties.ApplicationInfo;
import client.graphics.vk.instance.properties.DebugInfo;
import client.graphics.vk.instance.properties.InstanceExtension;
import client.graphics.vk.instance.properties.InstanceInfo;
import client.graphics.vk.instance.properties.InstanceLayer;
import client.graphics.vk.instance.properties.Version;
import client.graphics.vk.memory.MemoryContext;
import client.graphics.vk.models.Maskable;
import client.graphics.vk.models.function.CheckFunction;
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
import org.lwjgl.vulkan.VkPhysicalDevice;
import org.tinylog.Logger;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VulkanInstance extends DestroyableReferencePointer<VkInstance> {
    private final VkInstance handle;
    private final InstanceInfo info;
    private final long debugHandle;

    public VulkanInstance(ApplicationInfo applicationInfo, InstanceInfo info, DebugInfo debugInfo) {
        this.info = info;

        MemoryStack stack = MemoryContext.getStack();

        IntBuffer pAvailableApiVersion = stack.mallocInt(1);
        CheckFunction.execute(() -> VK11.vkEnumerateInstanceVersion(pAvailableApiVersion));
        Version availableApiVersion = Version.fromVulkanVersion(pAvailableApiVersion.get(0));

        if (availableApiVersion.compareTo(applicationInfo.getApiVersion()) < 0) {
            throw new RuntimeException("Available api version " + availableApiVersion + " is lower than required api version " + applicationInfo.getApiVersion());
        }

        Logger.debug("API Version: {}", availableApiVersion);

        VkExtensionProperties.Buffer pAvailableExtensions = EnumerateFunction.execute(stack.mallocInt(1),
                (pCount, pBuffer) -> VK10.vkEnumerateInstanceExtensionProperties((String) null, pCount, pBuffer),
                (count) -> VkExtensionProperties.malloc(count, stack));

        List<InstanceExtension> requiredExtensions = new ArrayList<>(info.getRequiredExtensions());
        List<InstanceExtension> optionalExtensions = new ArrayList<>(info.getOptionalExtensions());
        List<InstanceExtension> availableExtensions = InstanceExtension.fromVulkanExtensions(pAvailableExtensions);
        List<InstanceExtension> enabledExtensions = info.getEnabledExtensions();

        Logger.debug("Found {} extensions", availableExtensions.size());

        for (InstanceExtension extension : availableExtensions) {
            Logger.trace(extension.getValue());

            int requiredIndex = requiredExtensions.indexOf(extension);
            if (requiredIndex != -1) {
                requiredExtensions.remove(requiredIndex);
                enabledExtensions.add(extension);
                continue;
            }

            int optionalIndex = optionalExtensions.indexOf(extension);
            if (optionalIndex != -1) {
                optionalExtensions.remove(optionalIndex);
                enabledExtensions.add(extension);
            }
        }

        if (!requiredExtensions.isEmpty()) {
            throw new RuntimeException("Failed to find required vulkan instance extensions: " + requiredExtensions
                    .stream()
                    .map(Enum::name)
                    .collect(Collectors.joining(", ")));
        }

        if (!optionalExtensions.isEmpty()) {
            Logger.debug("Could not find optional vulkan instance extensions: {}", optionalExtensions);
        }

        PointerBuffer pExtensions = InstanceExtension.toVulkanBuffer(enabledExtensions);

        VkLayerProperties.Buffer pAvailableLayers = EnumerateFunction.execute(stack.mallocInt(1),
                VK10::vkEnumerateInstanceLayerProperties,
                count -> VkLayerProperties.malloc(count, stack));

        List<InstanceLayer> requiredLayers = new ArrayList<>(info.getRequiredLayers());
        List<InstanceLayer> optionalLayers = new ArrayList<>(info.getOptionalLayers());
        List<InstanceLayer> availableLayers = InstanceLayer.fromVulkanExtensions(pAvailableLayers);
        List<InstanceLayer> enabledLayers = info.getEnabledLayers();

        Logger.debug("Found {} layers", availableLayers.size());

        for (InstanceLayer layer : availableLayers) {
            Logger.trace(layer.getValue());

            int requiredIndex = requiredLayers.indexOf(layer);
            if (requiredIndex != -1) {
                requiredLayers.remove(requiredIndex);
                enabledLayers.add(layer);
                continue;
            }

            int optionalIndex = optionalLayers.indexOf(layer);
            if (optionalIndex != -1) {
                optionalLayers.remove(optionalIndex);
                enabledLayers.add(layer);
            }
        }

        if (!requiredExtensions.isEmpty()) {
            throw new RuntimeException("Failed to find required vulkan instance layers: " + requiredLayers
                    .stream()
                    .map(Enum::name)
                    .collect(Collectors.joining(", ")));
        }

        if (!optionalExtensions.isEmpty()) {
            Logger.debug("Could not find optional vulkan instance extension: {}", optionalLayers);
        }

        PointerBuffer pLayers = InstanceLayer.toVulkanBuffer(enabledLayers);

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

        PointerBuffer pHandle = stack.mallocPointer(1);
        CheckFunction.execute(() -> VK10.vkCreateInstance(createInfo, null, pHandle));
        handle = new VkInstance(pHandle.get(0), createInfo);

        if (debugCreateInfo != null) {
            LongBuffer pDebugHandle = stack.mallocLong(1);
            CheckFunction.execute(() -> EXTDebugReport.vkCreateDebugReportCallbackEXT(handle, debugCreateInfo, null, pDebugHandle));
            debugHandle = pDebugHandle.get(0);
        } else {
            debugHandle = -1L;
        }
    }

    public List<PhysicalDevice> getPhysicalDevices() {
        MemoryStack stack = MemoryContext.getStack();
        PointerBuffer pPhysicalDevices = EnumerateFunction.execute(stack.mallocInt(1),
                (pCount, pBuffer) -> VK10.vkEnumeratePhysicalDevices(handle, pCount, pBuffer),
                stack::mallocPointer);

        int physicalDeviceCount = pPhysicalDevices.capacity();
        List<PhysicalDevice> physicalDevices = new ArrayList<>(physicalDeviceCount);

        for (int i = 0; i < physicalDeviceCount; i++) {
            physicalDevices.add(new PhysicalDevice(new VkPhysicalDevice(pPhysicalDevices.get(i), handle)));
        }

        return physicalDevices;
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

    public InstanceInfo getInfo() {
        return info;
    }
}
