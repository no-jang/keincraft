package engine.graphics.instance;

import engine.graphics.instance.properties.MessageSeverity;
import engine.util.enums.Maskable;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkDebugReportCallbackEXT;
import org.lwjgl.vulkan.VkDebugReportCallbackEXTI;
import org.tinylog.Logger;
import org.tinylog.TaggedLogger;

public class InstanceLogger implements VkDebugReportCallbackEXTI {
    private final TaggedLogger logger = Logger.tag("VALIDATION_LAYER");

    @Override
    public int invoke(int flags, int objectType, long object, long location, int messageCode, long pLayerPrefix, long pMessage, long pUserData) {
        MessageSeverity severity = Maskable.fromBit(flags, MessageSeverity.class);

        if (severity == null) {
            throw new IllegalArgumentException("Unknown message severity: " + flags);
        }

        String prefix = MemoryUtil.memASCII(pLayerPrefix);
        String message = VkDebugReportCallbackEXT.getString(pMessage);
        String log = "{}: {} {}";

        if (severity == MessageSeverity.PERFORMANCE_WARNING) {
            log += "performance ";
        }

        switch (severity.getLevel()) {
            case DEBUG:
                logger.debug(log, prefix, messageCode, message);
                break;
            case INFO:
                logger.info(log, prefix, messageCode, message);
                break;
            case WARN:
                logger.warn(log, prefix, messageCode, message);
                break;
            case ERROR:
                logger.error(log, prefix, messageCode, message);
                break;
            default:
                throw new IllegalStateException("Should never be reached");
        }

        return VK10.VK_FALSE;
    }
}
