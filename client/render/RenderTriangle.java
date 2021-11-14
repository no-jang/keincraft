package client.render;

import client.render.vk.Global;
import client.render.vk.draw.*;
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
import client.render.vk.setup.queue.QueueFamily;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.KHRSwapchain;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkPresentInfoKHR;
import org.lwjgl.vulkan.VkSubmitInfo;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final int MAX_FRAMES_IN_FLIGHT = 2;

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

        List<Semaphore> imageAvailableSemaphores = new ArrayList<>(MAX_FRAMES_IN_FLIGHT);
        List<Semaphore> renderFinishedSemaphores = new ArrayList<>(MAX_FRAMES_IN_FLIGHT);
        List<Fence> inFlightFences = new ArrayList<>(MAX_FRAMES_IN_FLIGHT);
        Map<Integer, Fence> imagesInFlight;
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

            // TODO Recreate swapchain on window resize
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

            // TODO Add builder for building command buffers easy
            commandBuffers = new CommandBuffers(stack, device, commandPool, renderpass, swapchain, pipeline, framebuffers);

            imagesInFlight = new HashMap<>(swapchain.getImageCount());

            for (int i = 0; i < MAX_FRAMES_IN_FLIGHT; i++) {
                imageAvailableSemaphores.add(new Semaphore(stack, device));
                renderFinishedSemaphores.add(new Semaphore(stack, device));
                inFlightFences.add(new Fence(stack, device));
            }
        }

        int currentFrame = 0;

        // TODO Make smaller
        while (!window.shouldClose()) {
            window.input();

            try (MemoryStack stack = MemoryStack.stackPush()) {
                VK10.vkWaitForFences(device.getHandle(), inFlightFences.get(currentFrame).getHandle(), true, ~0L);

                IntBuffer pImageIndex = stack.mallocInt(1);
                Global.vkCheck(KHRSwapchain.vkAcquireNextImageKHR(device.getHandle(), swapchain.getHandle(), ~0L, imageAvailableSemaphores.get(currentFrame).getHandle(), VK10.VK_NULL_HANDLE, pImageIndex),
                        "Failed to acquire next image from swapchain");
                int imageIndex = pImageIndex.get(0);

                if (imagesInFlight.containsKey(imageIndex)) {
                    VK10.vkWaitForFences(device.getHandle(), imagesInFlight.get(imageIndex).getHandle(), true, ~0L);
                }

                imagesInFlight.put(imageIndex, inFlightFences.get(currentFrame));

                VkSubmitInfo submitInfo = VkSubmitInfo.malloc(stack)
                        .sType$Default()
                        .pNext(0)
                        .waitSemaphoreCount(1)
                        .pWaitSemaphores(stack.longs(imageAvailableSemaphores.get(currentFrame).getHandle()))
                        .pWaitDstStageMask(stack.ints(VK10.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT))
                        .pCommandBuffers(stack.pointers(commandBuffers.getHandles().get(imageIndex)))
                        .pSignalSemaphores(stack.longs(renderFinishedSemaphores.get(currentFrame).getHandle()));

                VK10.vkResetFences(device.getHandle(), inFlightFences.get(currentFrame).getHandle());

                Global.vkCheck(VK10.vkQueueSubmit(graphicsQueue.getHandle(), submitInfo, inFlightFences.get(currentFrame).getHandle()), "Failed to submit to graphics queue");

                VkPresentInfoKHR presentInfo = VkPresentInfoKHR.calloc(stack)
                        .sType$Default()
                        .pNext(0)
                        .pWaitSemaphores(stack.longs(renderFinishedSemaphores.get(currentFrame).getHandle()))
                        .swapchainCount(1)
                        .pSwapchains(stack.longs(swapchain.getHandle()))
                        .pImageIndices(stack.ints(imageIndex))
                        .pResults(null);

                Global.vkCheck(KHRSwapchain.vkQueuePresentKHR(presentQueue.getHandle(), presentInfo), "Failed to submit to present queue");
                VK10.vkQueueWaitIdle(presentQueue.getHandle());

                currentFrame = (currentFrame + 1) % MAX_FRAMES_IN_FLIGHT;
            }
        }

        VK10.vkDeviceWaitIdle(device.getHandle());

        for (int i = 0; i < MAX_FRAMES_IN_FLIGHT; i++) {
            imageAvailableSemaphores.get(i).destroy(device);
            renderFinishedSemaphores.get(i).destroy(device);
            inFlightFences.get(i).destroy(device);
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
