package engine.graphics.vulkan.device.properties;

import engine.helper.enums.HasValue;
import org.lwjgl.vulkan.KHRSwapchain;

public enum DeviceExtension implements HasValue<String> {
    KHR_SWAPCHAIN(KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME);

    private final String value;

    DeviceExtension(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
