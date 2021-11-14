package client.render;

import client.render.vk.pipeline.Pipeline;
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

import java.util.List;

public class RenderTriangle {
    public static void main(String[] args) {
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

        Shader vertexShader = Shader.readFromFile(device, ShaderType.VERTEX_SHADER, "base", "shaders/base.vert.spv");
        Shader fragmentShader = Shader.readFromFile(device, ShaderType.FRAGMENT_SHADER, "base", "shaders/base.frag.spv");

        Pipeline pipeline = new Pipeline(device, swapchain);

        vertexShader.destroy(device);
        fragmentShader.destroy(device);

        while (!window.shouldClose()) {
            window.input();
        }

        pipeline.destroy(device);

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
