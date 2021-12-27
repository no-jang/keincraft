package engine.graphics.vulkan.surface;

import engine.collections.list.DefaultImmutableList;
import engine.collections.list.ImmutableList;
import engine.graphics.vulkan.instance.Instance;
import engine.graphics.vulkan.surface.properties.PresentMode;
import engine.graphics.vulkan.surface.properties.SurfaceCapabilities;
import engine.graphics.vulkan.surface.properties.SurfaceFormat;
import engine.memory.test.ownable.DestroyableOwnablePointer;
import engine.window.Window;
import org.lwjgl.vulkan.KHRSurface;

import java.util.List;

public class Surface extends DestroyableOwnablePointer<Instance> {
    private final Window window;

    private final SurfaceCapabilities capabilities;
    private final ImmutableList<SurfaceFormat> formats;
    private final ImmutableList<PresentMode> presentModes;

    public Surface(Instance owner,
                   long address,
                   Window window,
                   SurfaceCapabilities capabilities,
                   List<SurfaceFormat> formats,
                   List<PresentMode> presentModes) {
        super(owner, address);
        this.window = window;
        this.capabilities = capabilities;
        this.formats = new DefaultImmutableList<>(formats);
        this.presentModes = new DefaultImmutableList<>(presentModes);
    }

    @Override
    protected void doDestroy() {
        KHRSurface.vkDestroySurfaceKHR(owner.unwrap(), address, null);
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

    public Window getWindow() {
        return window;
    }
}
