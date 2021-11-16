package client.render.vk.draw.submit;

import client.render.vk.Global;
import client.render.vk.device.Device;
import client.render.vk.draw.frame.Frame;
import client.render.vk.present.SwapChain;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.KHRSwapchain;
import org.lwjgl.vulkan.VK10;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

public class ImageAcquire {
    private final Map<Integer, Frame> framesInFlight;

    private boolean framebufferResized;

    public ImageAcquire(SwapChain swapchain) {
        framesInFlight = new HashMap<>(swapchain.getImageCount());
    }

    public int acquireImage(MemoryStack stack, Device device, SwapChain swapchain, Frame frame) {
        framebufferResized = false;

        IntBuffer pImageIndex = stack.mallocInt(1);
        framebufferResized = Global.vkCheckResized(KHRSwapchain.vkAcquireNextImageKHR(device.getHandle(), swapchain.getHandle(),
                        ~0L, frame.getImageAvailableSemaphore().getHandle(), VK10.VK_NULL_HANDLE, pImageIndex),
                "Failed to acquire next image index from swapChain");

        int imageIndex = pImageIndex.get(0);

        if (framesInFlight.containsKey(imageIndex)) {
            VK10.vkWaitForFences(device.getHandle(), framesInFlight.get(imageIndex).getInFlightFence().getHandle(), true, ~0L);
        }
        framesInFlight.put(imageIndex, frame);

        return imageIndex;
    }

    public boolean isFramebufferResized() {
        return framebufferResized;
    }
}
