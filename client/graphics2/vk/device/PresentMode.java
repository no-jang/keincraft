package client.graphics2.vk.device;

import org.lwjgl.vulkan.KHRSurface;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for VkPresentModeKHR
 */
public enum PresentMode {
    IMMEDIATE(KHRSurface.VK_PRESENT_MODE_IMMEDIATE_KHR),
    MAILBOX(KHRSurface.VK_PRESENT_MODE_MAILBOX_KHR),
    FIFO(KHRSurface.VK_PRESENT_MODE_FIFO_KHR),
    FIFO_RELAXED(KHRSurface.VK_PRESENT_MODE_FIFO_RELAXED_KHR);

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

    /**
     * Transforms a present mode int buffer into a list of PresentModes
     *
     * @param buffer buffer to transform
     * @return present mode list
     */
    public static List<PresentMode> fromBuffer(IntBuffer buffer) {
        List<PresentMode> modes = new ArrayList<>(buffer.capacity());
        for (int i = 0; i < buffer.capacity(); i++) {
            modes.add(valueOf(buffer.get(i)));
        }
        return modes;
    }

    /**
     * @return vulkan int code for corresponding present mode
     */
    public int getIndex() {
        return index;
    }
}
