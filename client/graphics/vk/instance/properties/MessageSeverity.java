package client.graphics.vk.instance.properties;

import common.util.enums.Maskable;
import org.lwjgl.vulkan.EXTDebugReport;
import org.tinylog.Level;

/**
 * Wrapper enum for vulkan debug report message severity. Severities are mapped to {@link Level}
 *
 * @see EXTDebugReport
 * @see Level
 */
public enum MessageSeverity implements Maskable {
    VERBOSE(EXTDebugReport.VK_DEBUG_REPORT_INFORMATION_BIT_EXT, Level.DEBUG),
    INFO(EXTDebugReport.VK_DEBUG_REPORT_INFORMATION_BIT_EXT, Level.INFO),
    WARNING(EXTDebugReport.VK_DEBUG_REPORT_WARNING_BIT_EXT, Level.WARN),
    PERFORMANCE_WARNING(EXTDebugReport.VK_DEBUG_REPORT_PERFORMANCE_WARNING_BIT_EXT, Level.WARN),
    ERROR(EXTDebugReport.VK_DEBUG_REPORT_ERROR_BIT_EXT, Level.ERROR);

    private final int bit;
    private final Level level;

    MessageSeverity(int bit, Level level) {
        this.bit = bit;
        this.level = level;
    }

    @Override
    public int getBit() {
        return bit;
    }

    /**
     * Returns tinylog {@link Level} corresponding to the vulkan message severity
     *
     * @return mapped tinylog {@link Level}
     */
    public Level getLevel() {
        return level;
    }
}
