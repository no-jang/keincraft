package engine.graphics.vulkan.swapchain;

import engine.graphics.vulkan.image.properties.ColorSpace;
import engine.graphics.vulkan.image.properties.Format;
import engine.graphics.vulkan.properties.Extent2D;
import engine.graphics.vulkan.surface.Surface;
import engine.graphics.vulkan.surface.properties.PresentMode;
import engine.graphics.vulkan.surface.properties.SurfaceFormat;
import engine.graphics.vulkan.surface.properties.SurfaceTransform;
import engine.graphics.vulkan.swapchain.properties.SwapchainInfo;
import engine.util.Maths;
import engine.window.Window;

public final class SwapchainUtil {
    public static SurfaceFormat chooseSurfaceFormat(Surface surface, SwapchainInfo info) {
        if (info.getFormat() != null) {
            return info.getFormat();
        }

        for (SurfaceFormat availableFormat : surface.getFormats()) {
            if (availableFormat.getFormat() == Format.B8G8R8A8_SRGB && availableFormat.getColorSpace() == ColorSpace.SRGB_NONLINEAR) {
                return availableFormat;
            }
        }

        return surface.getFormats().get(0);
    }

    public static Extent2D chooseExtent(Surface surface, SwapchainInfo info) {
        if (info.getImageExtent() != null) {
            return info.getImageExtent();
        }

        Extent2D currentExtent = surface.getCapabilities().getCurrentExtent();
        if (currentExtent.getWidth() != 0xFFFFFFFF) {
            return currentExtent;
        }

        Extent2D minImageExtent = surface.getCapabilities().getMinImageExtent();
        Extent2D maxImageExtent = surface.getCapabilities().getMaxImageExtent();

        Window window = surface.getWindow();

        int actualWidth = Maths.clamp(window.getWidth(), minImageExtent.getWidth(), maxImageExtent.getWidth());
        int actualHeight = Maths.clamp(window.getHeight(), minImageExtent.getHeight(), maxImageExtent.getHeight());

        return new Extent2D(actualWidth, actualHeight);
    }

    public static SurfaceTransform chooseTransform(Surface surface, SwapchainInfo info) {
        if (info.getSurfaceTransform() != null) {
            return info.getSurfaceTransform();
        }

        return surface.getCapabilities().getCurrentTransform();
    }

    public static PresentMode choosePresentMode(Surface surface, SwapchainInfo info) {
        if (info.getPresentMode() != null) {
            return info.getPresentMode();
        }

        if (surface.getPresentModes().contains(PresentMode.MAILBOX)) {
            return PresentMode.MAILBOX;
        } else {
            return PresentMode.FIFO;
        }
    }

    public static int chooseMinImageCount(Surface surface, SwapchainInfo info) {
        if (info.getMinImageCount() != -1) {
            return info.getMinImageCount();
        }

        int maxImageCount = surface.getCapabilities().getMaxImageCount();
        int minImageCount = surface.getCapabilities().getMinImageCount() + 1;

        if (maxImageCount > 0 && minImageCount > maxImageCount) {
            minImageCount = maxImageCount;
        }

        return minImageCount;
    }
}
