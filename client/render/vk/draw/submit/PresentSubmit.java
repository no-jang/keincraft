package client.render.vk.draw.submit;

import client.render.vk.Global;
import client.render.vk.device.queue.Queue;
import client.render.vk.draw.frame.Frame;
import client.render.vk.present.SwapChain;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.KHRSwapchain;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkPresentInfoKHR;

public final class PresentSubmit {
    public static void submitPresent(MemoryStack stack, SwapChain swapchain, Queue presentQueue, Frame frame, int imageIndex) {
        VkPresentInfoKHR presentInfo = VkPresentInfoKHR.calloc(stack)
                .sType$Default()
                .pNext(0)
                .pWaitSemaphores(stack.longs(frame.getRenderFinishedSemaphore().getHandle()))
                .swapchainCount(1)
                .pSwapchains(stack.longs(swapchain.getHandle()))
                .pImageIndices(stack.ints(imageIndex))
                .pResults(null);

        Global.vkCheck(KHRSwapchain.vkQueuePresentKHR(presentQueue.getHandle(), presentInfo),
                "Failed to submit to present queue");
        VK10.vkQueueWaitIdle(presentQueue.getHandle());
    }
}
