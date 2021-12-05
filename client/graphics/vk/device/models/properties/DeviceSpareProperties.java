package client.graphics.vk.device.models.properties;

import org.lwjgl.vulkan.VkPhysicalDeviceSparseProperties;

import java.util.ArrayList;
import java.util.List;

public enum DeviceSpareProperties {

    RESIDENCY_ALIGNED_MIP_SIZE,
    RESIDENCY_NON_RESIDENT_STRICT,
    RESIDENCY_STANDARD_2D_BLOCK_SHAPE,
    RESIDENCY_STANDARD_2D_MULTISAMPLE_BLOCK_SHAPE,
    RESIDENCY_STANDARD_3D_BLOCK_SHAPE;

    public static List<DeviceSpareProperties> fromVulkanSpareProperties(VkPhysicalDeviceSparseProperties vkProperties) {
        List<DeviceSpareProperties> properties = new ArrayList<>();

        if (vkProperties.residencyAlignedMipSize()) {
            properties.add(RESIDENCY_ALIGNED_MIP_SIZE);
        }

        if (vkProperties.residencyNonResidentStrict()) {
            properties.add(RESIDENCY_NON_RESIDENT_STRICT);
        }

        if (vkProperties.residencyStandard2DBlockShape()) {
            properties.add(RESIDENCY_STANDARD_2D_BLOCK_SHAPE);
        }

        if (vkProperties.residencyStandard2DMultisampleBlockShape()) {
            properties.add(RESIDENCY_STANDARD_2D_MULTISAMPLE_BLOCK_SHAPE);
        }

        if (vkProperties.residencyStandard3DBlockShape()) {
            properties.add(RESIDENCY_STANDARD_3D_BLOCK_SHAPE);
        }

        return properties;
    }
}
