package client.render;

import client.graphics.vk.device.Device;
import client.graphics.vk.device.Instance;
import client.graphics.vk.device.PhysicalDevice;
import client.graphics.vk.device.Surface;
import client.graphics.vk.pipeline.Pipeline;
import client.graphics.vk.pipeline.Shader;
import client.graphics.vk.renderpass.*;
import client.render.context.frame.FrameContext;
import client.render.vk.draw.cmd.CommandBuffers;
import client.render.vk.draw.cmd.CommandPool;
import client.render.vk.draw.submit.GraphicsSubmit;
import client.render.vk.draw.submit.PresentSubmit;
import org.lwjgl.system.MemoryStack;

import java.nio.file.Path;
import java.util.List;

// TODO Graphics context
// TODO Sub context
// TODO Add vertex buffers
// TODO Add uniform buffers
// TODO Add projection, model, view matrix
// TODO Add movement
// TODO Add textures
// TODO Add depth buffers
// TODO Load models
// TODO Generate texture mipmaps
// TODO Add multisampling (antialiasing)
// TODO Generate chunk mesh
// TODO Render chunk
public class RenderTriangle {
    private final client.graphics.vk.device.Window window;
    private final Instance instance;
    private final Surface surface;
    private final Device device;
    private final Renderpass renderPass;
    private Swapchain swapChain;
    private final Pipeline pipeline;
    private final List<Attachment> attachments;
    private Framebuffers framebuffers;
    private final CommandPool commandPool;
    private final CommandBuffers commandBuffers;
    private FrameContext frameContext;

    public RenderTriangle() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            window = new client.graphics.vk.device.Window("triangle", 900, 900);
            instance = new Instance(stack);
            PhysicalDevice physicalDevice = PhysicalDevice.getPhysicalDevice(stack, instance);
            surface = new Surface(stack, instance, physicalDevice, window);
            device = new Device(stack, physicalDevice, surface);

            createSwapChain(stack);

            List<Shader> shaders = List.of(
                    new Shader(stack, device, Path.of("shaders/base.vert.spv")),
                    new Shader(stack, device, Path.of("shaders/base.frag.spv"))
            );

            attachments = List.of(new Attachment(0, AttachmentType.Swapchain, surface.getFormat().format()));
            List<Subpass> subpasses = List.of(new Subpass(0, attachments));
            renderPass = new Renderpass(stack, device, subpasses, attachments);
            pipeline = new Pipeline(stack, device, swapChain, renderPass, shaders);

            for (Shader shader : shaders) {
                shader.destroy(device);
            }

            createFramebuffers(stack);

            commandPool = new CommandPool(stack, device);

            commandBuffers = new CommandBuffers(stack, device, commandPool, framebuffers);
            recordCommandBuffers();
        }
    }

    public static void main(String[] args) {
        RenderTriangle triangle = new RenderTriangle();
        triangle.loop();
        triangle.destroy();
    }

    public void createSwapChain(MemoryStack stack) {
        swapChain = new Swapchain(stack, device, surface, window, null);

        frameContext = new FrameContext(stack, device, 2);
    }

    public void createFramebuffers(MemoryStack stack) {
        framebuffers = new Framebuffers(stack, device, renderPass, swapChain, attachments);
    }

    public void recordCommandBuffers() {
        commandBuffers.record(buffer -> {
            buffer.setViewport(swapChain);
            buffer.beginRenderPass(swapChain, renderPass);
            buffer.beginPipeline(pipeline);
            buffer.draw(3, 1, 0, 0);
            buffer.endRenderPass();
        });
    }

    public void loop() {
        while (!window.shouldClose()) {
            window.input();

            try (MemoryStack stack = MemoryStack.stackPush()) {
                swapChain.acquireNextImage(stack, device, frameContext.getCurrentFrame());
                GraphicsSubmit.submitGraphics(stack, device, commandBuffers, swapChain, frameContext.getCurrentFrame());
                PresentSubmit.submitPresent(stack, device, swapChain, frameContext.getCurrentFrame());

                frameContext.nextFrame();
            }
        }
    }

/*    public void recreateSwapChain(MemoryStack stack) {
        window.gatherFramebufferSize();
        while (window.getWidth() == 0 || window.getHeight() == 0) {
            window.gatherFramebufferSize();
            GLFW.glfwWaitEvents();
        }

        destroySwapChain();
        createSwapChain(stack);
        createFramebuffers(stack);
        commandBuffers.reset();
        commandBuffers.update(framebuffers);
        recordCommandBuffers();
    }*/

    public void destroySwapChain() {
        device.waitIdle();

        frameContext.destroy(device);

        framebuffers.destroy(device);

        swapChain.destroy(device);
    }

    public void destroy() {
        destroySwapChain();

        commandPool.destroy(device);

        pipeline.destroy(device);
        renderPass.destroy(device);

        device.destroy();
        surface.destroy(instance);
        instance.destroy();
        window.destroy();
    }
}
