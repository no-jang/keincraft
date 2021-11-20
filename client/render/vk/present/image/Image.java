package client.render.vk.present.image;

import client.graphics.vk.device.Device;
import client.render.vk.present.SwapChain;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.KHRSwapchain;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;

import static client.render.vk.Global.vkCheck;

public class Image {
    private final long handle;

    public Image(long aHandle) {
        this.handle = aHandle;
    }

    public static List<Image> createImages(MemoryStack stack, Device device, SwapChain swapchain) {
        IntBuffer pImageCount = stack.mallocInt(1);
        vkCheck(KHRSwapchain.vkGetSwapchainImagesKHR(device.getHandle(), swapchain.getHandle(), pImageCount, null),
                "Failed to get swapchain image count");
        int imageCount = pImageCount.get(0);

        LongBuffer pImages = stack.mallocLong(imageCount);
        vkCheck(KHRSwapchain.vkGetSwapchainImagesKHR(device.getHandle(), swapchain.getHandle(), pImageCount, pImages),
                "Failed to get swapchain images");

        List<Image> images = new ArrayList<>(imageCount);
        for (int i = 0; i < imageCount; i++) {
            Image image = new Image(pImages.get(i));
            images.add(image);
        }

        return images;
    }

    public long getHandle() {
        return handle;
    }
}
