package engine.graphics.vulkan.instance.factory;

import engine.graphics.vulkan.helper.function.VkFunction;
import engine.graphics.vulkan.instance.Instance;
import engine.graphics.vulkan.instance.extension.ExtensionContainer;
import engine.graphics.vulkan.instance.extension.properties.InstanceExtension;
import engine.graphics.vulkan.instance.extension.properties.InstanceLayer;
import engine.graphics.vulkan.instance.properties.Version;
import engine.graphics.vulkan.instance.util.InstanceUtil;
import engine.memory.MemoryContext;
import engine.util.Buffers;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;

import java.nio.ByteBuffer;

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

        // Check if requested version is lower or equal available version
        Version availableVkVersion = InstanceUtil.currentVulkanVersion();
        if(availableVkVersion.compareTo(info.getVulkanVersion()) < 0) {
            throw new IllegalArgumentException("Requested vulkan version " + info.getVulkanVersion() + " is higher" +
                    "than available version " + availableVkVersion);
        }

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

        PointerBuffer handle = stack.mallocPointer(1);
        VkFunction.execute(() -> VK10.vkCreateInstance(createInfo, null, handle));
        VkInstance instance = new VkInstance(handle.get(0), createInfo);

        return new Instance(instance);
    }

    private VkApplicationInfo createApplicationInfo(MemoryStack stack, InstanceInfo info) {
        ByteBuffer applicationName = null;
        if(info.getApplicationName() != null) {
            applicationName = stack.ASCII(info.getApplicationName());
        }

        ByteBuffer engineName = null;
        if(info.getEngineName() != null) {
            engineName = stack.ASCII(info.getEngineName());
        }

        int applicationVersion = 0;
        if(info.getApplicationVersion() != null) {
            applicationVersion = info.getApplicationVersion().toVulkan();
        }

        int engineVersion = 0;
        if(info.getApplicationVersion() != null) {
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
        if(extensions == null) {
            return null;
        }

        return Buffers.toStringBuffer(stack, extensions.getRequestedExtensions().toMutable());
    }

    @Nullable
    private PointerBuffer createLayers(MemoryStack stack, @Nullable ExtensionContainer<InstanceLayer> layers) {
        if(layers == null) {
            return null;
        }

        return Buffers.toStringBuffer(stack, layers.getRequestedExtensions().toMutable());
    }
}
