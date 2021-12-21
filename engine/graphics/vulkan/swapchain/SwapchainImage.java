package engine.graphics.vulkan.swapchain;

import engine.util.pointer.DestroyablePointer;

public class SwapchainImage extends DestroyablePointer {
    public SwapchainImage(long handle) {
        super(handle);
    }

    @Override
    protected void destroy(long handle) {
        // Image is destroyed by swapchain
    }
}
