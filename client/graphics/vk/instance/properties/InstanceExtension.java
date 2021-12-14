package client.graphics.vk.instance.properties;

import common.util.enums.HasValue;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.EXTDebugReport;
import org.lwjgl.vulkan.KHRSurface;
import org.lwjgl.vulkan.VkExtensionProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper enum for vulkan instance extensions. The functionality for vulkan can be expanded through extensions which
 * may be available depending on your graphics processor and driver.
 */

// TODO add extensions for other window provider (like wayland, windows, macos)
public enum InstanceExtension implements HasValue<String> {
    DEBUG_REPORT(EXTDebugReport.VK_EXT_DEBUG_REPORT_EXTENSION_NAME),
    KHR_SURFACE(KHRSurface.VK_KHR_SURFACE_EXTENSION_NAME),
    KHR_XCB_SURFACE("VK_KHR_xcb_surface");

    private final String value;

    InstanceExtension(String value) {
        this.value = value;
    }

    /**
     * Converts a pointer buffer with extension names to InstanceExtensions.
     *
     * @param pBuffer buffer with extension names. Can't be null.
     * @return list of InstanceExtensions
     * @throws IllegalArgumentException Throws IllegalArgumentException if one extension name is not present in the enum
     */
    @NotNull
    public static List<InstanceExtension> fromNameBuffer(@NotNull PointerBuffer pBuffer) {
        List<InstanceExtension> extensions = new ArrayList<>(pBuffer.capacity());

        for (int i = 0; i < pBuffer.capacity(); i++) {
            String extensionName = MemoryUtil.memASCII(pBuffer.get(i));
            InstanceExtension extension = HasValue.getByValue(extensionName, InstanceExtension.class);

            if (extension == null) {
                throw new IllegalArgumentException("Unable to find extension with name in enum InstanceExtension: " + extensionName);
            }

            extensions.add(extension);
        }

        return extensions;
    }

    /**
     * Converts a VkExtensionProperties buffer to InstanceExtensions
     *
     * @param pExtensions VkExtensionsProperties buffer. Can't be null
     * @return InstanceExtensions
     * @see VkExtensionProperties.Buffer
     */
    @NotNull
    public static List<InstanceExtension> fromBuffer(@NotNull VkExtensionProperties.Buffer pExtensions) {
        List<InstanceExtension> extensions = new ArrayList<>();

        for (int i = 0; i < pExtensions.capacity(); i++) {
            InstanceExtension extension = HasValue.getByValue(pExtensions.get(i).extensionNameString(), InstanceExtension.class);
            if (extension != null) {
                extensions.add(extension);
            }
        }

        return extensions;
    }

    @Override
    public String getValue() {
        return value;
    }
}
