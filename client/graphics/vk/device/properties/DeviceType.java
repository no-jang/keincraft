package client.graphics.vk.device.properties;

import common.util.enums.HasValue;
import org.lwjgl.vulkan.VK10;

public enum DeviceType implements HasValue<Integer> {
    OTHER(VK10.VK_PHYSICAL_DEVICE_TYPE_OTHER),
    INTEGRATED_GPU(VK10.VK_PHYSICAL_DEVICE_TYPE_INTEGRATED_GPU),
    DISCRETE_GPU(VK10.VK_PHYSICAL_DEVICE_TYPE_DISCRETE_GPU),
    VIRTUAL_GPU(VK10.VK_PHYSICAL_DEVICE_TYPE_VIRTUAL_GPU),
    CPU(VK10.VK_PHYSICAL_DEVICE_TYPE_CPU);

    private final int value;

    DeviceType(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
