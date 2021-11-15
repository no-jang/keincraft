package client.render.vk.draw.submit;

import client.render.vk.Global;
import client.render.vk.device.Device;
import client.render.vk.device.queue.Queue;
import client.render.vk.draw.cmd.CommandBuffers;
import client.render.vk.draw.frame.Frame;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkSubmitInfo;

public final class GraphicsSubmit {
    public static void submitGraphics(MemoryStack stack, Device device, CommandBuffers commandBuffers, Queue graphicsQueue, Frame frame, int imageIndex) {
        VkSubmitInfo submitInfo = VkSubmitInfo.malloc(stack)
                .sType$Default()
                .pNext(0)
                .waitSemaphoreCount(1)
                .pWaitSemaphores(stack.longs(frame.getImageAvailableSemaphore().getHandle()))
                .pWaitDstStageMask(stack.ints(VK10.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT))
                .pCommandBuffers(stack.pointers(commandBuffers.getHandles().get(imageIndex)))
                .pSignalSemaphores(stack.longs(frame.getRenderFinishedSemaphore().getHandle()));

        VK10.vkResetFences(device.getHandle(), frame.getInFlightFence().getHandle());
        Global.vkCheck(VK10.vkQueueSubmit(graphicsQueue.getHandle(), submitInfo, frame.getInFlightFence().getHandle()),
                "Failed to submit to graphics queue");
    }
}
