import engine.collections.container.Container;
import engine.graphics.vulkan.device.Device;
import engine.graphics.vulkan.device.DeviceExtensionFactory;
import engine.graphics.vulkan.device.DeviceFactory;
import engine.graphics.vulkan.device.PhysicalDevice;
import engine.graphics.vulkan.device.PhysicalDeviceFactory;
import engine.graphics.vulkan.device.properties.DeviceExtension;
import engine.graphics.vulkan.device.properties.DeviceFeature;
import engine.graphics.vulkan.device.queue.QueueCapability;
import engine.graphics.vulkan.device.queue.QueueContainer;
import engine.graphics.vulkan.device.queue.QueueFactory;
import engine.graphics.vulkan.device.queue.QueueFamily;
import engine.graphics.vulkan.instance.Instance;
import engine.graphics.vulkan.instance.InstanceExtensionFactory;
import engine.graphics.vulkan.instance.InstanceFactory;
import engine.graphics.vulkan.instance.properties.InstanceExtension;
import engine.graphics.vulkan.instance.properties.InstanceInfo;
import engine.graphics.vulkan.instance.properties.InstanceLayer;
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

        InstanceExtensionFactory extensionFactory = new InstanceExtensionFactory();
        Container<InstanceExtension> instanceExtensions = extensionFactory.createExtensionContainer()
                .required(windowContext.getVulkanExtensions())
                .required(InstanceExtension.DEBUG_REPORT)
                .build();

        Container<InstanceLayer> instanceLayers = extensionFactory.createLayerContainer()
                .required(InstanceLayer.KHRONOS_VALIDATION)
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
        QueueContainer.Builder queueBuilder = queueFactory.createQueueFamilies(physicalDevice);

        QueueFamily graphicsFamily = queueBuilder.required(stream -> stream.filter(family -> family.getCapabilities().contains(QueueCapability.GRAPHICS)));
        QueueFamily presentFamily = queueBuilder.required(stream -> stream.filter(family -> family.hasPresentationSupport(surface)));

        QueueContainer queueContainer = queueBuilder.build();

        DeviceExtensionFactory deviceExtensionFactory = new DeviceExtensionFactory();
        Container<DeviceExtension> deviceExtensions = deviceExtensionFactory.createExtensionContainer(physicalDevice)
                .required(DeviceExtension.KHR_SWAPCHAIN)
                .build();

        Container<DeviceFeature> deviceFeatures = deviceExtensionFactory.createFeatureContainer(physicalDevice)
                .build();

        DeviceFactory deviceFactory = new DeviceFactory();
        Device device = deviceFactory.createDevice(physicalDevice, queueContainer, deviceExtensions, deviceFeatures);

        while (!window.isCloseRequested()) {
            windowContext.input();
        }

        device.destroy();
        surface.destroy();
        instance.destroy();
        window.destroy();
/*
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.addRequiredExtension(DeviceExtension.KHR_SWAPCHAIN);
        deviceInfo.addQueue(graphicsFamily, List.of(1.0f));
        deviceInfo.addQueue(presentFamily, List.of(1.0f));

        LogicalDevice device = new LogicalDevice(physicalDevice, deviceInfo);
        device.printDevice();

        device.destroy();*/
    }
}
