package client.render;

import client.graphics.device.Device;
import client.graphics.device.Instance;
import client.graphics.device.PhysicalDevice;
import client.graphics.device.Surface;
import client.render.context.frame.FrameContext;
import client.render.vk.draw.cmd.CommandBuffers;
import client.render.vk.draw.cmd.CommandPool;
import client.render.vk.draw.submit.GraphicsSubmit;
import client.render.vk.draw.submit.ImageAcquire;
import client.render.vk.draw.submit.PresentSubmit;
import client.render.vk.draw.sync.Framebuffer;
import client.render.vk.pipeline.Pipeline;
import client.render.vk.pipeline.RenderPass;
import client.render.vk.pipeline.part.*;
import client.render.vk.pipeline.shader.Shader;
import client.render.vk.pipeline.shader.ShaderType;
import client.render.vk.present.SwapChain;
import client.render.vk.present.image.Image;
import client.render.vk.present.image.ImageView;
import org.lwjgl.system.MemoryStack;

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
    private final client.graphics.device.Window window;
    private final Instance instance;
    private final PhysicalDevice physicalDevice;
    private final Surface surface;
    private final Device device;

    private SwapChain swapChain;
    private List<ImageView> imageViews;
    private final RenderPass renderPass;
    private final Pipeline pipeline;
    private List<Framebuffer> framebuffers;
    private final CommandPool commandPool;
    private final CommandBuffers commandBuffers;
    private FrameContext frameContext;
    private ImageAcquire imageAcquire;

    public RenderTriangle() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            window = new client.graphics.device.Window("triangle", 900, 900);
            instance = new Instance(stack);
            physicalDevice = PhysicalDevice.getPhysicalDevice(stack, instance);
            surface = new Surface(stack, instance, physicalDevice, window);
            device = new Device(stack, physicalDevice, surface);

            createSwapChain(stack);

            List<Shader> shaders = List.of(
                    Shader.readFromFile(stack, device, ShaderType.VERTEX_SHADER, "shaders/base.vert.spv"),
                    Shader.readFromFile(stack, device, ShaderType.FRAGMENT_SHADER, "shaders/base.frag.spv")
            );

            renderPass = new RenderPass(stack, device, surface);
            ColorBlend colorBlend = new ColorBlend(stack);
            Multisampling multisampling = new Multisampling(stack);
            Rasterizer rasterizer = new Rasterizer(stack);
            VertexInput vertexInput = new VertexInput(stack);
            DynamicState dynamicState = new DynamicState(stack);
            pipeline = new Pipeline(stack, device, swapChain, renderPass, shaders, vertexInput, rasterizer, multisampling, colorBlend, dynamicState);

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
        swapChain = new SwapChain(stack, device, surface, window);
        List<Image> images = Image.createImages(stack, device, swapChain);
        imageViews = ImageView.createImageViews(stack, device, swapChain, surface, images);

        imageAcquire = new ImageAcquire(swapChain);
        frameContext = new FrameContext(stack, device, 2);
    }

    public void createFramebuffers(MemoryStack stack) {
        framebuffers = Framebuffer.createFramebuffers(stack, device, renderPass, swapChain, imageViews);
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
                int imageIndex = imageAcquire.acquireImage(stack, device, swapChain, frameContext.getCurrentFrame());
                boolean framebufferResized = imageAcquire.isFramebufferResized();
                framebufferResized = GraphicsSubmit.submitGraphics(stack, device, commandBuffers, frameContext.getCurrentFrame(), imageIndex);
                framebufferResized = PresentSubmit.submitPresent(stack, device, swapChain, frameContext.getCurrentFrame(), imageIndex);

                if (framebufferResized) {
                    //recreateSwapChain(stack);
                }

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

        for (Framebuffer framebuffer : framebuffers) {
            framebuffer.destroy(device);
        }

        for (ImageView view : imageViews) {
            view.destroy(device);
        }

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
