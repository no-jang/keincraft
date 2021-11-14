package client.render;

import client.render.vk.Global;
import client.render.vk.draw.CommandBuffers;
import client.render.vk.draw.CommandPool;
import client.render.vk.draw.Framebuffer;
import client.render.vk.draw.Semaphore;
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
import org.lwjgl.vulkan.KHRSwapchain;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkPresentInfoKHR;
import org.lwjgl.vulkan.VkSubmitInfo;

import java.nio.IntBuffer;
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

            for (Shader shader : shaders) {
                shader.destroy(device);
            }

            List<Framebuffer> framebuffers = Framebuffer.createFramebuffers(device, renderpass, swapchain, imageViews);

            CommandPool commandPool = new CommandPool(physicalDevice, device);
            CommandBuffers commandBuffers = new CommandBuffers(device, commandPool, renderpass, swapchain, pipeline, framebuffers);

            Semaphore imageAvailableSemaphore = new Semaphore(device);
            Semaphore renderFinishedSemaphore = new Semaphore(device);

            while (!window.shouldClose()) {
                window.input();

                try (MemoryStack frameStack = MemoryStack.stackPush()) {
                    IntBuffer pImageIndex = frameStack.mallocInt(1);
                    Global.vkCheck(KHRSwapchain.vkAcquireNextImageKHR(device.getHandle(), swapchain.getHandle(), ~0L, imageAvailableSemaphore.getHandle(), VK10.VK_NULL_HANDLE, pImageIndex),
                            "Failed to acquire next image from swapchain");
                    int imageIndex = pImageIndex.get(0);

                    VkSubmitInfo submitInfo = VkSubmitInfo.malloc(frameStack)
                            .sType$Default()
                            .pNext(0)
                            .waitSemaphoreCount(1)
                            .pWaitSemaphores(frameStack.longs(imageAvailableSemaphore.getHandle()))
                            .pWaitDstStageMask(frameStack.ints(VK10.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT))
                            .pCommandBuffers(frameStack.pointers(commandBuffers.getHandles().get(imageIndex)))
                            .pSignalSemaphores(frameStack.longs(renderFinishedSemaphore.getHandle()));

                    Global.vkCheck(VK10.vkQueueSubmit(queue.getHandle(), submitInfo, VK10.VK_NULL_HANDLE), "Failed to submit to graphics queue");

                    VkPresentInfoKHR presentInfo = VkPresentInfoKHR.calloc(frameStack)
                            .sType$Default()
                            .pNext(0)
                            .pWaitSemaphores(frameStack.longs(renderFinishedSemaphore.getHandle()))
                            .swapchainCount(1)
                            .pSwapchains(frameStack.longs(swapchain.getHandle()))
                            .pImageIndices(frameStack.ints(imageIndex))
                            .pResults(null);

                    Global.vkCheck(KHRSwapchain.vkQueuePresentKHR(queue.getHandle(), presentInfo), "Failed to submit to present queue");
                }
            }

            VK10.vkDeviceWaitIdle(device.getHandle());

            imageAvailableSemaphore.destroy(device);
            renderFinishedSemaphore.destroy(device);

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
