package engine.graphics.vulkan.instance;

import engine.collections.container.Container;
import engine.collections.container.DefaultContainer;
import engine.graphics.vulkan.helper.function.VkFunction;
import engine.graphics.vulkan.instance.properties.InstanceExtension;
import engine.graphics.vulkan.instance.properties.InstanceLayer;
import engine.memory.EnumBuffers;
import engine.memory.MemoryContext;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkExtensionProperties;
import org.lwjgl.vulkan.VkLayerProperties;

import java.nio.IntBuffer;
import java.util.List;

public class InstanceExtensionFactory {
    public static Container.Builder<InstanceExtension> createExtensionContainer() {
        MemoryStack stack = MemoryContext.getStack();

        IntBuffer availableExtensionCountBuffer = stack.mallocInt(1);
        VkFunction.execute(() -> VK10.vkEnumerateInstanceExtensionProperties((String) null, availableExtensionCountBuffer, null));
        int availableExtensionCount = availableExtensionCountBuffer.get(0);

        VkExtensionProperties.Buffer availableExtensionsBuffer = VkExtensionProperties.malloc(availableExtensionCount, stack);
        VkFunction.execute(() -> VK10.vkEnumerateInstanceExtensionProperties((String) null, availableExtensionCountBuffer, availableExtensionsBuffer));
        List<InstanceExtension> availableExtensions =
                EnumBuffers.ofStruct(availableExtensionsBuffer, InstanceExtension.class, VkExtensionProperties::extensionNameString);

        return new DefaultContainer.Builder<>(availableExtensions);
    }

    public static DefaultContainer.Builder<InstanceLayer> createLayerContainer() {
        MemoryStack stack = MemoryContext.getStack();

        IntBuffer availableLayerCountBuffer = stack.mallocInt(1);
        VkFunction.execute(() -> VK10.vkEnumerateInstanceLayerProperties(availableLayerCountBuffer, null));
        int availableLayerCount = availableLayerCountBuffer.get(0);

        VkLayerProperties.Buffer availableLayerBuffer = VkLayerProperties.malloc(availableLayerCount, stack);
        VkFunction.execute(() -> VK10.vkEnumerateInstanceLayerProperties(availableLayerCountBuffer, availableLayerBuffer));
        List<InstanceLayer> availableLayers =
                EnumBuffers.ofStruct(availableLayerBuffer, InstanceLayer.class, VkLayerProperties::layerNameString);

        return new DefaultContainer.Builder<>(availableLayers);
    }
}
