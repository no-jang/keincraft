import engine.graphics.vulkan.instance.Instance;
import engine.graphics.vulkan.instance.InstanceFactory;
import engine.graphics.vulkan.instance.extension.ExtensionContainer;
import engine.graphics.vulkan.instance.extension.ExtensionFactory;
import engine.graphics.vulkan.instance.extension.properties.InstanceExtension;
import engine.graphics.vulkan.instance.extension.properties.InstanceLayer;
import engine.graphics.vulkan.instance.properties.InstanceInfo;
import engine.graphics.vulkan.instance.properties.MessageSeverity;
import engine.graphics.vulkan.instance.properties.Version;
import engine.window.Window;
import engine.window.WindowContext;
import engine.window.WindowFactory;
import engine.window.properties.WindowInfo;

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

        while (!window.isCloseRequested()) {
            windowContext.input();
        }

        instance.destroy();
        window.destroy();

/*
        List<PhysicalDevice> physicalDevices = instance.getPhysicalDevices();
        PhysicalDevice physicalDevice = physicalDevices.get(0);
        physicalDevice.printDevice();

        Surface surface = new Surface(instance, window, physicalDevice);

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

        device.destroy();
        surface.destroy();
        instance.destroy();*/
    }
}
