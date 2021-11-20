package client.render.vk.draw.submit;

import client.graphics.device.Device;
import client.graphics.renderpass.SwapChain;
import client.graphics.sync.Frame;
import client.render.vk.Global;
import client.render.vk.draw.cmd.CommandBuffers;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkSubmitInfo;

public final class GraphicsSubmit {
    public static boolean submitGraphics(MemoryStack stack, Device device, CommandBuffers commandBuffers, SwapChain swapChain, Frame frame) {
        VkSubmitInfo submitInfo = VkSubmitInfo.malloc(stack)
                .sType$Default()
                .pNext(0)
                .waitSemaphoreCount(1)
                .pWaitSemaphores(stack.longs(frame.getImageAvailableSemaphore().getHandle()))
                .pWaitDstStageMask(stack.ints(VK10.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT))
                .pCommandBuffers(stack.pointers(commandBuffers.getBuffer(swapChain.getActiveImageIndex()).getHandle()))
                .pSignalSemaphores(stack.longs(frame.getRenderFinishedSemaphore().getHandle()));

        VK10.vkResetFences(device.getHandle(), frame.getInFlightFence().getHandle());
        return Global.vkCheckResized(VK10.vkQueueSubmit(device.getGraphicsQueue(), submitInfo, frame.getInFlightFence().getHandle()),
                "Failed to submit to graphics queue");
    }
}
