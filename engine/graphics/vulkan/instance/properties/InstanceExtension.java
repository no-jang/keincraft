package engine.graphics.vulkan.instance.properties;

import engine.helper.enums.HasValue;
import org.lwjgl.vulkan.EXTDebugReport;
import org.lwjgl.vulkan.KHRSurface;

public enum InstanceExtension implements HasValue<String> {
    DEBUG_REPORT(EXTDebugReport.VK_EXT_DEBUG_REPORT_EXTENSION_NAME),
    KHR_SURFACE(KHRSurface.VK_KHR_SURFACE_EXTENSION_NAME),
    KHR_XCB_SURFACE("VK_KHR_xcb_surface");

    private final String value;

    InstanceExtension(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
