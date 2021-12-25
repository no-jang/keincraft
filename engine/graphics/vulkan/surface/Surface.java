package engine.graphics.vulkan.surface;

import engine.collections.list.DefaultImmutableList;
import engine.collections.list.ImmutableList;
import engine.graphics.vulkan.instance.Instance;
import engine.graphics.vulkan.surface.properties.PresentMode;
import engine.graphics.vulkan.surface.properties.SurfaceCapabilities;
import engine.graphics.vulkan.surface.properties.SurfaceFormat;
import engine.util.pointer.DestroyablePointer;
import engine.window.Window;
import org.lwjgl.vulkan.KHRSurface;

import java.util.List;

public class Surface extends DestroyablePointer {
    private final Instance instance;
    private final Window window;

    private final SurfaceCapabilities capabilities;
    private final ImmutableList<SurfaceFormat> formats;
    private final ImmutableList<PresentMode> presentModes;

    public Surface(long handle, Instance instance, Window window, SurfaceCapabilities capabilities, List<SurfaceFormat> formats, List<PresentMode> presentModes) {
        super(handle);
        this.instance = instance;
        this.window = window;
        this.capabilities = capabilities;
        this.formats = new DefaultImmutableList<>(formats);
        this.presentModes = new DefaultImmutableList<>(presentModes);
    }

    @Override
    protected void destroy(long handle) {
        KHRSurface.vkDestroySurfaceKHR(instance.getReference(), handle, null);
    }

    public SurfaceCapabilities getCapabilities() {
        return capabilities;
    }

    public ImmutableList<SurfaceFormat> getFormats() {
        return formats;
    }

    public ImmutableList<PresentMode> getPresentModes() {
        return presentModes;
    }

    public Instance getInstance() {
        return instance;
    }

    public Window getWindow() {
        return window;
    }
}
