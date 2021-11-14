package client.render;

import client.render.vk.draw.CommandBuffers;
import client.render.vk.draw.CommandPool;
import client.render.vk.draw.Framebuffer;
import client.render.vk.pipeline.Pipeline;
import client.render.vk.pipeline.Renderpass;
import client.render.vk.pipeline.Shader;
import client.render.vk.pipeline.ShaderType;
import client.render.vk.present.Image;
import client.render.vk.present.ImageView;
import client.render.vk.present.Surface;
import client.render.vk.present.Swapchain;
import client.render.vk.setup.Device;
import client.render.vk.setup.Instance;
import client.render.vk.setup.PhysicalDevice;
import client.render.vk.setup.queue.Queue;
import org.lwjgl.system.MemoryStack;

import java.util.List;

public class RenderTriangle {
    public static void main(String[] args) {
        //TODO Find solutions to remove this
        try (MemoryStack stack = MemoryStack.stackPush()) {
            Window window = new Window(900, 900);
            Instance instance = new Instance();
            Surface surface = new Surface(instance, window);

            PhysicalDevice physicalDevice = PhysicalDevice.pickPhysicalDevice(instance, surface);
            Queue queue = new Queue(physicalDevice.getQueueFamilies().getFamilyIndex());
            Device device = new Device(physicalDevice, queue);

            queue.setup(device);

            Swapchain swapchain = new Swapchain(physicalDevice, device, surface, window);
            List<Image> images = Image.createImages(device, swapchain);
            List<ImageView> imageViews = ImageView.createImageViews(device, swapchain, images);

            List<Shader> shaders = List.of(
                    Shader.readFromFile(stack, device, ShaderType.VERTEX_SHADER, "shaders/base.vert.spv"),
                    Shader.readFromFile(stack, device, ShaderType.FRAGMENT_SHADER, "shaders/base.frag.spv")
            );

            Renderpass renderpass = new Renderpass(device, swapchain);
            Pipeline pipeline = new Pipeline(device, swapchain, renderpass, shaders);

            List<Framebuffer> framebuffers = Framebuffer.createFramebuffers(device, renderpass, swapchain, imageViews);

            CommandPool commandPool = new CommandPool(physicalDevice, device);
            CommandBuffers commandBuffers = new CommandBuffers(device, commandPool, renderpass, swapchain, pipeline, framebuffers);

            for (Shader shader : shaders) {
                shader.destroy(device);
            }

            while (!window.shouldClose()) {
                window.input();
            }

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
}
