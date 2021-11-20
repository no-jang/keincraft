package client.render.vk.draw.submit;

import client.graphics.device.Device;
import client.graphics.renderpass.SwapChain;
import client.graphics.sync.Frame;
import client.render.vk.Global;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.KHRSwapchain;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkPresentInfoKHR;

public final class PresentSubmit {
    public static boolean submitPresent(MemoryStack stack, Device device, SwapChain swapchain, Frame frame) {
        VkPresentInfoKHR presentInfo = VkPresentInfoKHR.calloc(stack)
                .sType$Default()
                .pNext(0)
                .pWaitSemaphores(stack.longs(frame.getRenderFinishedSemaphore().getHandle()))
                .swapchainCount(1)
                .pSwapchains(stack.longs(swapchain.getHandle()))
                .pImageIndices(stack.ints(swapchain.getActiveImageIndex()))
                .pResults(null);

        boolean framebufferResized = Global.vkCheckResized(KHRSwapchain.vkQueuePresentKHR(device.getPresentQueue(), presentInfo),
                "Failed to submit to present queue");

        VK10.vkQueueWaitIdle(device.getPresentQueue());

        return framebufferResized;
    }
}
