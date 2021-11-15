package client.render.vk.draw.submit;

import client.render.vk.Global;
import client.render.vk.device.Device;
import client.render.vk.draw.frame.Frame;
import client.render.vk.present.Swapchain;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.KHRSwapchain;
import org.lwjgl.vulkan.VK10;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

public class ImageAcquire {
    private final Map<Integer, Frame> framesInFlight;

    public ImageAcquire(Swapchain swapchain) {
        framesInFlight = new HashMap<>(swapchain.getImageCount());
    }

    public int acquireImage(MemoryStack stack, Device device, Swapchain swapchain, Frame frame) {
        IntBuffer pImageIndex = stack.mallocInt(1);
        Global.vkCheck(KHRSwapchain.vkAcquireNextImageKHR(device.getHandle(), swapchain.getHandle(), ~0L, frame.getImageAvailableSemaphore().getHandle(), VK10.VK_NULL_HANDLE, pImageIndex),
                "Failed to acquire next image index from swapchain");
        int imageIndex = pImageIndex.get(0);

        if (framesInFlight.containsKey(imageIndex)) {
            VK10.vkWaitForFences(device.getHandle(), framesInFlight.get(imageIndex).getInFlightFence().getHandle(), true, ~0L);
        }
        framesInFlight.put(imageIndex, frame);

        return imageIndex;
    }
}
