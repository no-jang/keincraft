package engine.graphics.vulkan.instance.properties;

import engine.util.enums.Maskable;
import org.lwjgl.vulkan.EXTDebugReport;
import org.tinylog.Level;

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

    public Level getLevel() {
        return level;
    }
}
