package client.graphics.vk.device.models;

import client.graphics.vk.memory.MemoryContext;
import org.lwjgl.vulkan.VkPhysicalDeviceFeatures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public enum DeviceFeature {
    GEOMETRY_SHADER,
    SHADER_CLIP_DISTANCE;

    public static List<DeviceFeature> fromVulkanFeatures(VkPhysicalDeviceFeatures vkFeatures) {
        List<DeviceFeature> features = new ArrayList<>();

        if (vkFeatures.geometryShader()) {
            features.add(GEOMETRY_SHADER);
        }

        if (vkFeatures.shaderClipDistance()) {
            features.add(SHADER_CLIP_DISTANCE);
        }

        return features;
    }

    public static VkPhysicalDeviceFeatures toVulkanFeatures(Collection<DeviceFeature> features) {
        VkPhysicalDeviceFeatures vkFeatures = VkPhysicalDeviceFeatures.calloc(MemoryContext.getStack());

        for (DeviceFeature feature : features) {
            switch (feature) {
                case GEOMETRY_SHADER:
                    vkFeatures.geometryShader(true);
                    break;
                case SHADER_CLIP_DISTANCE:
                    vkFeatures.shaderClipDistance(true);
                    break;
            }
        }

        return vkFeatures;
    }
}
