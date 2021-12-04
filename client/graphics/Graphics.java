package client.graphics;

import client.graphics.vk.cmd.CommandBuffer;
import client.graphics.vk.cmd.CommandPool;
import client.graphics.vk.device.Device;
import client.graphics.vk.device.Instance;
import client.graphics.vk.device.PhysicalDevice;
import client.graphics.vk.device.Surface;
import client.graphics.vk.pipeline.Pipeline;
import client.graphics.vk.pipeline.Shader;
import client.graphics.vk.renderpass.Framebuffer;
import client.graphics.vk.renderpass.Renderpass;
import client.graphics.vk.renderpass.Swapchain;
import org.lwjgl.system.MemoryStack;

import java.nio.file.Path;
import java.util.List;

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
    private final Renderpass renderpass;
    private final List<Framebuffer> framebuffers;
    private final Pipeline pipeline;
    private final CommandPool commandPool;
    private final List<CommandBuffer> commandBuffers;

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
            renderpass = new Renderpass(stack, device, swapchain);
            framebuffers = swapchain.getFramebuffers(stack, device, renderpass);

            Shader[] shaders = new Shader[]{
                    new Shader(stack, device, Path.of("shaders/base.vert.spv")),
                    new Shader(stack, device, Path.of("shaders/base.frag.spv"))
            };

            pipeline = new Pipeline(stack, device, swapchain, renderpass, shaders);

            for (Shader shader : shaders) {
                shader.destroy(device);
            }

            commandPool = new CommandPool(stack, device);
            commandBuffers = commandPool.requestBuffers(stack, device, swapchain.getImageCount());

            for (int i = 0; i < swapchain.getImageCount(); i++) {
                CommandBuffer buffer = commandBuffers.get(i);
                buffer.begin(stack);
                buffer.beginRenderpass(stack, swapchain, renderpass, framebuffers.get(i));
                buffer.bindPipeline(pipeline);
                buffer.draw(3, 1, 0, 0);
                buffer.endRenderpass();
                buffer.end();
            }
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
        commandPool.destroy(device);
        pipeline.destroy(device);

        for (Framebuffer framebuffer : framebuffers) {
            framebuffer.getImageView().destroy(device);
            framebuffer.destroy(device);
        }

        renderpass.destroy(device);
        swapchain.destroy(device);
        device.destroy();
        surface.destroy(instance);
        instance.destroy();
    }
}
