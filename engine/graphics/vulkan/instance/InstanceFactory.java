package engine.graphics.vulkan.instance;

import engine.collections.container.Container;
import engine.graphics.vulkan.helper.function.VkFunction;
import engine.graphics.vulkan.instance.properties.InstanceExtension;
import engine.graphics.vulkan.instance.properties.InstanceInfo;
import engine.graphics.vulkan.instance.properties.InstanceLayer;
import engine.graphics.vulkan.instance.properties.InstanceProperties;
import engine.graphics.vulkan.instance.properties.Version;
import engine.helper.enums.Maskable;
import engine.memory.EnumBuffers;
import engine.memory.MemoryContext;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.EXTDebugReport;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkDebugReportCallbackCreateInfoEXT;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;

public class InstanceFactory {
    public static Instance create(InstanceInfo info,
                                  Container<InstanceExtension> extensions,
                                  Container<InstanceLayer> layers) {
        MemoryStack stack = MemoryContext.getStack();

        checkVkVersion(info);

        VkApplicationInfo applicationInfo = createApplicationInfo(stack, info);

        PointerBuffer extensionNames = EnumBuffers.toString(stack, extensions.getRequested().toMutable());
        PointerBuffer layerNames = EnumBuffers.toString(stack, layers.getRequested().toMutable());

        VkInstanceCreateInfo createInfo = VkInstanceCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .pApplicationInfo(applicationInfo)
                .ppEnabledExtensionNames(extensionNames)
                .ppEnabledLayerNames(layerNames);

        VkDebugReportCallbackCreateInfoEXT callbackCreateInfo = createMessageCallback(stack, info, createInfo);

        PointerBuffer handle = stack.mallocPointer(1);
        VkFunction.execute(() -> VK10.vkCreateInstance(createInfo, null, handle));
        VkInstance instance = new VkInstance(handle.get(0), createInfo);

        long messageCallbackHandle = finishMessageCallback(stack, instance, callbackCreateInfo);

        InstanceProperties properties = new InstanceProperties(extensions, layers);

        return new Instance(instance, properties, messageCallbackHandle);
    }

    private static void checkVkVersion(InstanceInfo info) {
        Version availableVkVersion = InstanceUtil.currentVulkanVersion();
        if (availableVkVersion.compareTo(info.getVulkanVersion()) < 0) {
            throw new IllegalArgumentException("Requested vulkan version " + info.getVulkanVersion() + " is higher" +
                    "than available version " + availableVkVersion);
        }
    }

    private static VkApplicationInfo createApplicationInfo(MemoryStack stack, InstanceInfo info) {
        ByteBuffer applicationName = null;
        if (info.getApplicationName() != null) {
            applicationName = stack.ASCII(info.getApplicationName());
        }

        ByteBuffer engineName = null;
        if (info.getEngineName() != null) {
            engineName = stack.ASCII(info.getEngineName());
        }

        int applicationVersion = 0;
        if (info.getApplicationVersion() != null) {
            applicationVersion = info.getApplicationVersion().toVulkan();
        }

        int engineVersion = 0;
        if (info.getApplicationVersion() != null) {
            engineVersion = info.getApplicationVersion().toVulkan();
        }

        return VkApplicationInfo.malloc(stack)
                .sType$Default()
                .pNext(0)
                .pApplicationName(applicationName)
                .pEngineName(engineName)
                .applicationVersion(applicationVersion)
                .engineVersion(engineVersion)
                .apiVersion(info.getVulkanVersion().toVulkan());
    }

    @Nullable
    private static VkDebugReportCallbackCreateInfoEXT createMessageCallback(MemoryStack stack, InstanceInfo info, VkInstanceCreateInfo createInfo) {
        if (info.getDebugSeverities().isEmpty()) {
            return null;
        }

        VkDebugReportCallbackCreateInfoEXT callbackCreateInfo = VkDebugReportCallbackCreateInfoEXT.malloc(stack)
                .sType$Default()
                .flags(Maskable.toBitMask(info.getDebugSeverities()))
                .pNext(0)
                .pfnCallback(new InstanceLogger())
                .pUserData(0);

        createInfo.pNext(callbackCreateInfo);
        return callbackCreateInfo;
    }

    private static long finishMessageCallback(MemoryStack stack, VkInstance instance,
                                              @Nullable VkDebugReportCallbackCreateInfoEXT callbackCreateInfo) {
        if (callbackCreateInfo == null) {
            return -1;
        }

        LongBuffer handle = stack.mallocLong(1);
        VkFunction.execute(() -> EXTDebugReport.vkCreateDebugReportCallbackEXT(instance, callbackCreateInfo, null, handle));
        return handle.get(0);
    }
}
