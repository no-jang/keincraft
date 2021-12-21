package engine.graphics.vulkan.device;

import engine.collections.container.Container;
import engine.collections.container.DefaultContainer;
import engine.graphics.vulkan.device.properties.DeviceExtension;
import engine.graphics.vulkan.device.properties.DeviceFeature;
import engine.graphics.vulkan.util.function.VkFunction;
import engine.memory.EnumBuffers;
import engine.memory.MemoryContext;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkExtensionProperties;
import org.lwjgl.vulkan.VkPhysicalDeviceFeatures;

import java.nio.IntBuffer;
import java.util.List;

public class DeviceExtensionFactory {
    public static Container.Builder<DeviceExtension> createExtensionContainer(PhysicalDevice device) {
        MemoryStack stack = MemoryContext.getStack();

        IntBuffer extensionCountBuffer = stack.mallocInt(1);
        VkFunction.execute(() -> VK10.vkEnumerateDeviceExtensionProperties(device.getReference(), (String) null, extensionCountBuffer, null));
        int extensionCount = extensionCountBuffer.get(0);

        VkExtensionProperties.Buffer extensionBuffer = VkExtensionProperties.malloc(extensionCount, stack);
        VkFunction.execute(() -> VK10.vkEnumerateDeviceExtensionProperties(device.getReference(), (String) null, extensionCountBuffer, extensionBuffer));
        List<DeviceExtension> extensions = EnumBuffers.ofStruct(extensionBuffer, DeviceExtension.class, VkExtensionProperties::extensionNameString);

        return new DefaultContainer.Builder<>(extensions);
    }

    public static Container.Builder<DeviceFeature> createFeatureContainer(PhysicalDevice device) {
        MemoryStack stack = MemoryContext.getStack();

        VkPhysicalDeviceFeatures vkFeatures = VkPhysicalDeviceFeatures.malloc(stack);
        VK10.vkGetPhysicalDeviceFeatures(device.getReference(), vkFeatures);
        List<DeviceFeature> features = DeviceFeature.ofVk(vkFeatures);

        return new DefaultContainer.Builder<>(features);
    }
}
