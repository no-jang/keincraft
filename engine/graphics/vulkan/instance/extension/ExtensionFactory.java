package engine.graphics.vulkan.instance.extension;

import engine.graphics.vulkan.helper.function.VkFunction;
import engine.graphics.vulkan.instance.extension.properties.InstanceExtension;
import engine.graphics.vulkan.instance.extension.properties.InstanceLayer;
import engine.memory.MemoryContext;
import engine.util.Buffers;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkExtensionProperties;
import org.lwjgl.vulkan.VkLayerProperties;

import java.nio.IntBuffer;
import java.util.List;

public class ExtensionFactory {
    public ExtensionContainer.Builder<InstanceExtension> createExtensionContainer() {
        MemoryStack stack = MemoryContext.getStack();

        IntBuffer availableExtensionCountBuffer = stack.mallocInt(1);
        VkFunction.execute(() -> VK10.vkEnumerateInstanceExtensionProperties((String) null, availableExtensionCountBuffer, null));
        int availableExtensionCount = availableExtensionCountBuffer.get(0);

        VkExtensionProperties.Buffer availableExtensionsBuffer = VkExtensionProperties.malloc(availableExtensionCount, stack);
        VkFunction.execute(() -> VK10.vkEnumerateInstanceExtensionProperties((String) null, availableExtensionCountBuffer, availableExtensionsBuffer));
        List<InstanceExtension> availableExtensions =
                Buffers.fromStructBuffer(availableExtensionsBuffer, InstanceExtension.class, VkExtensionProperties::extensionNameString);

        return new ExtensionContainer.Builder<>(availableExtensions);
    }

    public ExtensionContainer.Builder<InstanceLayer> createLayerContainer() {
        MemoryStack stack = MemoryContext.getStack();

        IntBuffer availableLayerCountBuffer = stack.mallocInt(1);
        VkFunction.execute(() -> VK10.vkEnumerateInstanceLayerProperties(availableLayerCountBuffer, null));
        int availableLayerCount = availableLayerCountBuffer.get(0);

        VkLayerProperties.Buffer availableLayerBuffer = VkLayerProperties.malloc(availableLayerCount, stack);
        VkFunction.execute(() -> VK10.vkEnumerateInstanceLayerProperties(availableLayerCountBuffer, availableLayerBuffer));
        List<InstanceLayer> availableLayers =
                Buffers.fromStructBuffer(availableLayerBuffer, InstanceLayer.class, VkLayerProperties::layerNameString);

        return new ExtensionContainer.Builder<>(availableLayers);
    }
}
