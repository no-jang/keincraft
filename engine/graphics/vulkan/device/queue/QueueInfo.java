package engine.graphics.vulkan.device.queue;

import java.util.List;

public class QueueInfo {
    private final QueueFamily family;
    private final List<Float> priorities;

    public QueueInfo(QueueFamily family, List<Float> priorities) {
        this.family = family;
        this.priorities = priorities;
    }

    public QueueFamily getFamily() {
        return family;
    }

    public List<Float> getPriorities() {
        return priorities;
    }

    @Override
    public String toString() {
        return "QueueInfo[" +
                "family=" + family +
                ", priorities=" + priorities +
                ']';
    }
}
