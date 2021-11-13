package client.render;

import client.render.vk.Instance;
import client.render.vk.device.Device;
import client.render.vk.device.PhysicalDevice;
import client.render.vk.device.queue.Queue;
import client.render.vk.present.Surface;
import client.render.vk.present.Swapchain;

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

        while (!window.shouldClose()) {
            window.input();
        }
    }
}
