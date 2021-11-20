package client.graphics;

import client.graphics.device.*;
import org.lwjgl.system.MemoryStack;

/**
 * Graphics module for managing everything regarded to window, graphics and rendering
 */
public class Graphics {
    private final Window window;
    private final Instance instance;
    private final PhysicalDevice physicalDevice;
    private final Surface surface;
    private final Device device;

    /**
     * Creates graphic module. Initializes vulkan and render infrastructure
     *
     * @param window window where it will be drawing to
     */
    public Graphics(Window window) {
        this.window = window;

        // Initialization memory stack for allocations in the graphics initialization phase
        try (MemoryStack stack = MemoryStack.stackPush()) {
            instance = new Instance(stack);
            physicalDevice = PhysicalDevice.getPhysicalDevice(stack, instance);
            surface = new Surface(stack, instance, physicalDevice, window);
            device = new Device(stack, physicalDevice, surface);
        }
    }

    /**
     * Destroys rendering architecture along with vulkan
     */
    public void destroy() {
        device.destroy();
        surface.destroy(instance);
        instance.destroy();
    }

    /**
     * @return wrapper for window where everything is being rendered to
     * @see Window
     */
    public Window getWindow() {
        return window;
    }

    /**
     * @return wrapper for vulkan instance
     * @see Instance
     */
    public Instance getInstance() {
        return instance;
    }

    /**
     * @return physical device which will be used for rendering and presentation
     * @see PhysicalDevice
     */
    public PhysicalDevice getPhysicalDevice() {
        return physicalDevice;
    }

    /**
     * @return surface where the images rendered will be presented
     * @see Surface
     */
    public Surface getSurface() {
        return surface;
    }

    /**
     * @return logical device which will be used for presentation and rendering
     * @see Device
     */
    public Device getDevice() {
        return device;
    }
}
