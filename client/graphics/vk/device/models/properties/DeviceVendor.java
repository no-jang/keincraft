package client.graphics.vk.device.models.properties;

import client.graphics.vk.models.HasValue;

public enum DeviceVendor implements HasValue<Integer> {
    NVIDIA(0x10DE),
    AMD(0x1002),
    INTEL(0x8086);

    private final int value;

    DeviceVendor(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
