package client.graphics.vk.instance.models;

import java.util.List;

public class DebugInfo {
    private final List<MessageSeverity> severities;

    public DebugInfo(List<MessageSeverity> severities) {
        this.severities = severities;
    }

    public List<MessageSeverity> getSeverities() {
        return severities;
    }
}
