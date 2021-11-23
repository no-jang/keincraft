package client.graphics;

import client.graphics.vk.device.Device;
import client.graphics.vk.device.Instance;
import client.graphics.vk.device.PhysicalDevice;
import client.graphics.vk.device.Surface;
import client.graphics.vk.renderpass.Swapchain;
import org.lwjgl.system.MemoryStack;

/**
 * Graphics module for managing everything regarded to window, graphics and rendering
 */
// TODO Outsource all swapchain size change effected things
public class Graphics {
    private final Window window;
    private final Instance instance;
    private final PhysicalDevice physicalDevice;
    private final Surface surface;
    private final Device device;
    private final Swapchain swapchain;

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
            surface = new Surface(stack, instance, physicalDevice, window);
            device = new Device(stack, physicalDevice, surface);
            swapchain = new Swapchain(stack, device, surface, window, null);
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
        swapchain.destroy(device);
        device.destroy();
        surface.destroy(instance);
        instance.destroy();
    }
}
