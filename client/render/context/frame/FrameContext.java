package client.render.context.frame;

import client.graphics.vk.device.Device;
import client.graphics.vk.sync.Frame;
import org.lwjgl.system.MemoryStack;

import java.util.ArrayList;
import java.util.List;

public class FrameContext {
    private final int framesInFlight;
    private final List<Frame> frames;

    private int currentFrame;

    public FrameContext(MemoryStack stack, Device device, int framesInFlight) {
        this.framesInFlight = framesInFlight;
        frames = new ArrayList<>(framesInFlight);
        for (int i = 0; i < framesInFlight; i++) {
            frames.add(new Frame(stack, device));
        }
    }

    public void destroy(Device device) {
        for (Frame frame : frames) {
            frame.destroy(device);
        }
    }

    public void nextFrame() {
        currentFrame = (currentFrame + 1) % framesInFlight;
    }

    public int getCurrentFrameIndex() {
        return currentFrame;
    }

    public Frame getCurrentFrame() {
        return frames.get(currentFrame);
    }
}
