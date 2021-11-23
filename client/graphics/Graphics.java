package client.graphics;

import client.graphics.vk.device.Instance;
import client.graphics.vk.device.PhysicalDevice;
import org.lwjgl.system.MemoryStack;

/**
 * Graphics module for managing everything regarded to window, graphics and rendering
 */
public class Graphics {
    private final Window window;
    private final Instance instance;
    private final PhysicalDevice physicalDevice;

    /**
     * Creates new graphics module for window
     *
     * @param window window to render to
     */
    public Graphics(Window window) {
        this.window = window;

        // Initialization memory stack
        try (MemoryStack stack = MemoryStack.stackPush()) {
            instance = new Instance(stack);
            physicalDevice = PhysicalDevice.getBestPhysicalDevice(stack, instance);
        }
    }

    /**
     * Updates all content regarded the next frame rendered
     */
    public void update() {

    }

    /**
     * Renders one frame
     */
    public void render() {

    }

    /**
     * Destroys every graphics component
     */
    public void destroy() {
        instance.destroy();
    }
}
