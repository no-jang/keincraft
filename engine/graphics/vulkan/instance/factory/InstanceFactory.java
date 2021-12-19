package engine.graphics.vulkan.instance.factory;

import engine.graphics.vulkan.helper.function.VkFunction;
import engine.graphics.vulkan.instance.Instance;
import engine.graphics.vulkan.instance.InstanceLogger;
import engine.graphics.vulkan.instance.extension.ExtensionContainer;
import engine.graphics.vulkan.instance.extension.properties.InstanceExtension;
import engine.graphics.vulkan.instance.extension.properties.InstanceLayer;
import engine.graphics.vulkan.instance.properties.Version;
import engine.graphics.vulkan.instance.util.InstanceUtil;
import engine.helper.enums.Maskable;
import engine.memory.MemoryContext;
import engine.util.Buffers;
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
    public Instance create(InstanceInfo info) {
        return create(info, null, null);
    }

    public Instance create(InstanceInfo info, @Nullable ExtensionContainer<InstanceExtension> extensions) {
        return create(info, extensions, null);
    }

    public Instance create(InstanceInfo info,
                           @Nullable ExtensionContainer<InstanceExtension> extensions,
                           @Nullable ExtensionContainer<InstanceLayer> layers) {
        MemoryStack stack = MemoryContext.getStack();

        checkVkVersion(info);

        VkApplicationInfo applicationInfo = createApplicationInfo(stack, info);

        PointerBuffer extensionNames = createExtensions(stack, extensions);
        PointerBuffer layerNames = createLayers(stack, layers);

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

        return new Instance(instance, messageCallbackHandle);
    }

    private void checkVkVersion(InstanceInfo info) {
        Version availableVkVersion = InstanceUtil.currentVulkanVersion();
        if (availableVkVersion.compareTo(info.getVulkanVersion()) < 0) {
            throw new IllegalArgumentException("Requested vulkan version " + info.getVulkanVersion() + " is higher" +
                    "than available version " + availableVkVersion);
        }
    }

    private VkApplicationInfo createApplicationInfo(MemoryStack stack, InstanceInfo info) {
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
    private PointerBuffer createExtensions(MemoryStack stack, @Nullable ExtensionContainer<InstanceExtension> extensions) {
        if (extensions == null) {
            return null;
        }

        return Buffers.toStringBuffer(stack, extensions.getRequestedExtensions().toMutable());
    }

    @Nullable
    private PointerBuffer createLayers(MemoryStack stack, @Nullable ExtensionContainer<InstanceLayer> layers) {
        if (layers == null) {
            return null;
        }

        return Buffers.toStringBuffer(stack, layers.getRequestedExtensions().toMutable());
    }

    @Nullable
    private VkDebugReportCallbackCreateInfoEXT createMessageCallback(MemoryStack stack, InstanceInfo info, VkInstanceCreateInfo createInfo) {
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

    private long finishMessageCallback(MemoryStack stack, VkInstance instance,
                                       @Nullable VkDebugReportCallbackCreateInfoEXT callbackCreateInfo) {
        if (callbackCreateInfo == null) {
            return -1;
        }

        LongBuffer handle = stack.mallocLong(1);
        VkFunction.execute(() -> EXTDebugReport.vkCreateDebugReportCallbackEXT(instance, callbackCreateInfo, null, handle));
        return handle.get(0);
    }
}
