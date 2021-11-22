package client.graphics2.vk.sync;

import client.graphics2.vk.device.Device;
import org.lwjgl.system.MemoryStack;

/**
 * Represents all synchronization objects for one frame
 */
public class Frame {
    private final Semaphore imageAvailableSemaphore;
    private final Semaphore renderFinishedSemaphore;
    private final Fence inFlightFence;

    /**
     * Creates synchronization objects
     *
     * @param stack  memory stack
     * @param device vulkan device
     */
    public Frame(MemoryStack stack, Device device) {
        this.imageAvailableSemaphore = new Semaphore(stack, device);
        this.renderFinishedSemaphore = new Semaphore(stack, device);
        this.inFlightFence = new Fence(stack, device);
    }

    /**
     * Destroy all synchronization objects
     *
     * @param device vulkan device
     */
    public void destroy(Device device) {
        imageAvailableSemaphore.destroy(device);
        renderFinishedSemaphore.destroy(device);
        inFlightFence.destroy(device);
    }

    /**
     * semaphore that waits until an image was presented and is ready to be acquired again
     *
     * @return image available semaphore
     */
    public Semaphore getImageAvailableSemaphore() {
        return imageAvailableSemaphore;
    }

    /**
     * semaphore that waits until an image is finished rendering
     *
     * @return render finished semaphore
     */
    public Semaphore getRenderFinishedSemaphore() {
        return renderFinishedSemaphore;
    }

    /**
     * fence that tells if the image is currently processed (in flight)
     *
     * @return in flight fence
     */
    public Fence getInFlightFence() {
        return inFlightFence;
    }
}
