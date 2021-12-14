package client.graphics.vk.instance.properties;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Debug information to determine how to set up vulkan debug reporter used by validation layers to print out errors.
 *
 * @see MessageSeverity
 */
public class DebugInfo {
    private final List<MessageSeverity> severities;

    /**
     * New debug information with message severity levels to determine if a message is important enough to be printed
     *
     * @param severities message severity levels. Can be null
     * @see MessageSeverity
     */
    public DebugInfo(@Nullable List<MessageSeverity> severities) {
        this.severities = severities;
    }

    /**
     * Return the message severity levels. The logger will check if the message has one of the configured severities
     * and only prints it then.
     *
     * @return message severity levels
     * @see MessageSeverity
     */
    @Nullable
    public List<MessageSeverity> getSeverities() {
        return severities;
    }
}
