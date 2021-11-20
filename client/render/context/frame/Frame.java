package client.render.context.frame;

import client.graphics.device.Device;
import client.render.vk.draw.sync.Fence;
import client.render.vk.draw.sync.Semaphore;
import org.lwjgl.system.MemoryStack;

public class Frame {
    private final Semaphore imageAvailableSemaphore;
    private final Semaphore renderFinishedSemaphore;
    private final Fence inFlightFence;

    public Frame(MemoryStack stack, Device device) {
        this.imageAvailableSemaphore = new Semaphore(stack, device);
        this.renderFinishedSemaphore = new Semaphore(stack, device);
        this.inFlightFence = new Fence(stack, device);
    }

    public void destroy(Device device) {
        imageAvailableSemaphore.destroy(device);
        renderFinishedSemaphore.destroy(device);
        inFlightFence.destroy(device);
    }

    public Semaphore getImageAvailableSemaphore() {
        return imageAvailableSemaphore;
    }

    public Semaphore getRenderFinishedSemaphore() {
        return renderFinishedSemaphore;
    }

    public Fence getInFlightFence() {
        return inFlightFence;
    }
}
