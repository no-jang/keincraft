package client.render;

import client.render.vk.device.Device;
import client.render.vk.device.Instance;
import client.render.vk.device.PhysicalDevice;
import client.render.vk.device.queue.Queue;
import client.render.vk.device.queue.QueueFamily;
import client.render.vk.draw.cmd.CommandBuffers;
import client.render.vk.draw.cmd.CommandPool;
import client.render.vk.draw.frame.FrameContext;
import client.render.vk.draw.submit.GraphicsSubmit;
import client.render.vk.draw.submit.ImageAcquire;
import client.render.vk.draw.submit.PresentSubmit;
import client.render.vk.draw.sync.Framebuffer;
import client.render.vk.pipeline.Pipeline;
import client.render.vk.pipeline.Renderpass;
import client.render.vk.pipeline.shader.Shader;
import client.render.vk.pipeline.shader.ShaderType;
import client.render.vk.present.Surface;
import client.render.vk.present.Swapchain;
import client.render.vk.present.image.Image;
import client.render.vk.present.image.ImageView;
import org.lwjgl.system.MemoryStack;

import java.util.List;

// TODO Make pipeline more configurable
// TODO Make command buffers more configurable
// TODO Recreate swapchain on window resize
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
    public static void main(String[] args) {
        Window window;
        Surface surface;
        Instance instance;
        Queue graphicsQueue;
        Queue presentQueue;
        Swapchain swapchain;
        Pipeline pipeline;
        Renderpass renderpass;
        Device device;
        CommandBuffers commandBuffers;
        CommandPool commandPool;
        FrameContext frameContext;
        ImageAcquire imageAcquire;

        List<ImageView> imageViews;
        List<Framebuffer> framebuffers;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            window = new Window(900, 900);
            instance = new Instance(stack);
            surface = new Surface(stack, instance, window);

            PhysicalDevice physicalDevice = PhysicalDevice.pickPhysicalDevice(stack, instance, surface);
            List<QueueFamily> queueFamilies = QueueFamily.createQueueFamilies(stack, physicalDevice.getQueueFamilies());
            device = new Device(stack, physicalDevice, queueFamilies);

            graphicsQueue = Queue.createQueue(stack, device, queueFamilies, physicalDevice.getQueueFamilies().getGraphicsFamilyIndex());
            presentQueue = Queue.createQueue(stack, device, queueFamilies, physicalDevice.getQueueFamilies().getPresentFamilyIndex());

            swapchain = new Swapchain(stack, physicalDevice, device, surface, window);
            List<Image> images = Image.createImages(stack, device, swapchain);
            imageViews = ImageView.createImageViews(stack, device, swapchain, images);

            List<Shader> shaders = List.of(
                    Shader.readFromFile(stack, device, ShaderType.VERTEX_SHADER, "shaders/base.vert.spv"),
                    Shader.readFromFile(stack, device, ShaderType.FRAGMENT_SHADER, "shaders/base.frag.spv")
            );

            renderpass = new Renderpass(stack, device, swapchain);
            pipeline = new Pipeline(stack, device, swapchain, renderpass, shaders);

            for (Shader shader : shaders) {
                shader.destroy(device);
            }

            framebuffers = Framebuffer.createFramebuffers(stack, device, renderpass, swapchain, imageViews);

            commandPool = new CommandPool(stack, physicalDevice, device);

            commandBuffers = new CommandBuffers(stack, device, commandPool, renderpass, swapchain, pipeline, framebuffers);

            frameContext = new FrameContext(stack, device, 2);
            imageAcquire = new ImageAcquire(swapchain);
        }

        while (!window.shouldClose()) {
            window.input();

            try (MemoryStack stack = MemoryStack.stackPush()) {
                int imageIndex = imageAcquire.acquireImage(stack, device, swapchain, frameContext.getCurrentFrame());
                GraphicsSubmit.submitGraphics(stack, device, commandBuffers, graphicsQueue, frameContext.getCurrentFrame(), imageIndex);
                PresentSubmit.submitPresent(stack, swapchain, presentQueue, frameContext.getCurrentFrame(), imageIndex);
                frameContext.nextFrame();
            }
        }

        device.waitIdle();
        frameContext.destroy(device);

        commandPool.destroy(device);

        for (Framebuffer framebuffer : framebuffers) {
            framebuffer.destroy(device);
        }

        pipeline.destroy(device);
        renderpass.destroy(device);

        for (ImageView view : imageViews) {
            view.destroy(device);
        }

        swapchain.destroy(device);
        device.destroy();
        surface.destroy(instance);
        instance.destroy();
        window.destroy();
    }
}
