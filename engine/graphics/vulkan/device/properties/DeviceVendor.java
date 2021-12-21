package engine.graphics.vulkan.device.properties;

import engine.util.enums.HasValue;

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