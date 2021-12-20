package engine.graphics.vulkan.surface;

import engine.graphics.vulkan.instance.Instance;
import engine.graphics.vulkan.surface.properties.PresentMode;
import engine.graphics.vulkan.surface.properties.SurfaceCapabilities;
import engine.graphics.vulkan.surface.properties.SurfaceFormat;
import engine.helper.pointer.DestroyablePointer;
import org.lwjgl.vulkan.KHRSurface;

import java.util.List;

public class Surface extends DestroyablePointer {
    private final Instance instance;

    private final SurfaceCapabilities capabilities;
    private final List<SurfaceFormat> formats;
    private final List<PresentMode> presentModes;

    public Surface(Instance instance, long handle, SurfaceCapabilities capabilities, List<SurfaceFormat> formats, List<PresentMode> presentModes) {
        super(handle);
        this.instance = instance;
        this.capabilities = capabilities;
        this.formats = formats;
        this.presentModes = presentModes;
    }

    @Override
    protected void destroy(long handle) {
        KHRSurface.vkDestroySurfaceKHR(instance.getReference(), handle, null);
    }

    public SurfaceCapabilities getCapabilities() {
        return capabilities;
    }

    public List<SurfaceFormat> getFormats() {
        return formats;
    }

    public List<PresentMode> getPresentModes() {
        return presentModes;
    }

    public Instance getInstance() {
        return instance;
    }
}
