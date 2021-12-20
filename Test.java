import engine.collections.list.ImmutableList;
import engine.graphics.vulkan.device.PhysicalDevice;
import engine.graphics.vulkan.device.PhysicalDeviceFactory;
import engine.graphics.vulkan.device.queue.QueueCapability;
import engine.graphics.vulkan.device.queue.QueueFactory;
import engine.graphics.vulkan.device.queue.QueueFamily;
import engine.graphics.vulkan.instance.Instance;
import engine.graphics.vulkan.instance.InstanceFactory;
import engine.graphics.vulkan.instance.extension.ExtensionContainer;
import engine.graphics.vulkan.instance.extension.ExtensionFactory;
import engine.graphics.vulkan.instance.extension.properties.InstanceExtension;
import engine.graphics.vulkan.instance.extension.properties.InstanceLayer;
import engine.graphics.vulkan.instance.properties.InstanceInfo;
import engine.graphics.vulkan.instance.properties.MessageSeverity;
import engine.graphics.vulkan.instance.properties.Version;
import engine.graphics.vulkan.surface.Surface;
import engine.graphics.vulkan.surface.SurfaceFactory;
import engine.window.Window;
import engine.window.WindowContext;
import engine.window.WindowFactory;
import engine.window.properties.WindowInfo;
import org.tinylog.Logger;

import java.util.List;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        WindowFactory windowFactory = new WindowFactory();
        WindowContext windowContext = windowFactory.createWindowContext();

        WindowInfo windowInfo = new WindowInfo.Builder()
                .title("test")
                .build();

        Window window = windowFactory.createWindow(windowContext, windowInfo);

        ExtensionFactory extensionFactory = new ExtensionFactory();
        ExtensionContainer<InstanceExtension> instanceExtensions = extensionFactory.createExtensionContainer()
                .request(windowContext.getVulkanExtensions())
                .request(InstanceExtension.DEBUG_REPORT)
                .build();

        ExtensionContainer<InstanceLayer> instanceLayers = extensionFactory.createLayerContainer()
                .request(InstanceLayer.KHRONOS_VALIDATION)
                .build();

        InstanceInfo instanceInfo = new InstanceInfo.Builder(new Version(1, 2, 0))
                .applicationName("test")
                .engineName("engine")
                .applicationVersion(new Version(1, 0, 0))
                .engineVersion(new Version(1, 0, 0))
                .debugSeverities(
                        MessageSeverity.ERROR,
                        MessageSeverity.WARNING,
                        MessageSeverity.PERFORMANCE_WARNING,
                        MessageSeverity.INFO,
                        MessageSeverity.VERBOSE
                )
                .build();

        InstanceFactory instanceFactory = new InstanceFactory();
        Instance instance = instanceFactory.create(instanceInfo, instanceExtensions, instanceLayers);

        PhysicalDeviceFactory physicalDeviceFactory = new PhysicalDeviceFactory();
        List<PhysicalDevice> physicalDevices = physicalDeviceFactory.createPhysicalDevices(instance);
        PhysicalDevice physicalDevice = physicalDevices.get(0);
        Logger.info(physicalDevice.toPropertyString());

        SurfaceFactory surfaceFactory = new SurfaceFactory();
        Surface surface = surfaceFactory.createSurface(instance, physicalDevice, window);

        QueueFactory queueFactory = new QueueFactory();
        ImmutableList<QueueFamily> queueFamilies = queueFactory.createQueueFamilies(physicalDevice);

        QueueFamily graphicsFamily = queueFamilies.stream()
                .filter(family -> family.getCount() > 0 && family.getCapabilities().contains(QueueCapability.GRAPHICS))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Failed to find graphics family"));

        QueueFamily presentFamily = queueFamilies.stream()
                .filter(family -> family.getCount() > 0 && family.hasPresentationSupport(surface))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Failed to find present family"));

        while (!window.isCloseRequested()) {
            windowContext.input();
        }

        surface.destroy();
        instance.destroy();
        window.destroy();
/*

        QueueFamily graphicsFamily = physicalDevice.getQueueFamilies().stream()
                .filter(family -> family.getQueueCount() > 0)
                .filter(family -> family.getCapabilities().contains(QueueCapability.GRAPHICS))
                .findFirst()
                .orElse(null);

        QueueFamily presentFamily = physicalDevice.getQueueFamilies().stream()
                .filter(family -> family.getQueueCount() > 0)
                .filter(family -> family.supportsPresentation(surface))
                .findFirst()
                .orElse(null);

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.addRequiredExtension(DeviceExtension.KHR_SWAPCHAIN);
        deviceInfo.addQueue(graphicsFamily, List.of(1.0f));
        deviceInfo.addQueue(presentFamily, List.of(1.0f));

        LogicalDevice device = new LogicalDevice(physicalDevice, deviceInfo);
        device.printDevice();

        device.destroy();*/
    }
}
