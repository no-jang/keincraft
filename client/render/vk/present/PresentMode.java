package client.render.vk.present;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public enum PresentMode {
    IMMEDIATE(0),
    MAILBOX(1),
    FIFO(2),
    FIFO_RELAXED(3);

    private final int index;

    PresentMode(int index) {
        this.index = index;
    }

    public static PresentMode valueOf(int index) {
        if (!(index >= 0 && index <= 3)) {
            throw new IllegalArgumentException("Present mode index must be between 0 and 3");
        }

        for (PresentMode mode : values()) {
            if (mode.index == index) {
                return mode;
            }
        }

        // Should never be reached
        return null;
    }

    public static List<PresentMode> fromBuffer(IntBuffer buffer) {
        List<PresentMode> modes = new ArrayList<>(buffer.capacity());
        for (int i = 0; i < buffer.capacity(); i++) {
            modes.add(valueOf(buffer.get(i)));
        }
        return modes;
    }
}
