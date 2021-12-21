import engine.collections.container.Container;
import engine.graphics.vulkan.device.Device;
import engine.graphics.vulkan.device.DeviceExtensionFactory;
import engine.graphics.vulkan.device.DeviceFactory;
import engine.graphics.vulkan.device.PhysicalDevice;
import engine.graphics.vulkan.device.PhysicalDeviceFactory;
import engine.graphics.vulkan.device.properties.DeviceExtension;
import engine.graphics.vulkan.device.properties.DeviceFeature;
import engine.graphics.vulkan.instance.Instance;
import engine.graphics.vulkan.instance.InstanceExtensionFactory;
import engine.graphics.vulkan.instance.InstanceFactory;
import engine.graphics.vulkan.instance.properties.InstanceExtension;
import engine.graphics.vulkan.instance.properties.InstanceInfo;
import engine.graphics.vulkan.instance.properties.InstanceLayer;
import engine.graphics.vulkan.instance.properties.MessageSeverity;
import engine.graphics.vulkan.instance.properties.Version;
import engine.graphics.vulkan.queue.Queue;
import engine.graphics.vulkan.queue.QueueFactory;
import engine.graphics.vulkan.queue.QueueFamily;
import engine.graphics.vulkan.queue.properties.QueueCapability;
import engine.graphics.vulkan.queue.properties.QueueContainer;
import engine.graphics.vulkan.surface.Surface;
import engine.graphics.vulkan.surface.SurfaceFactory;
import engine.graphics.vulkan.swapchain.Swapchain;
import engine.graphics.vulkan.swapchain.SwapchainFactory;
import engine.graphics.vulkan.swapchain.SwapchainImage;
import engine.graphics.vulkan.swapchain.properties.SwapchainInfo;
import engine.window.Window;
import engine.window.WindowContext;
import engine.window.WindowFactory;
import engine.window.properties.WindowInfo;
import org.tinylog.Logger;

import java.util.List;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        WindowContext windowContext = WindowFactory.createWindowContext();

        WindowInfo windowInfo = new WindowInfo.Builder()
                .title("test")
                .build();

        Window window = WindowFactory.createWindow(windowContext, windowInfo);

        Container<InstanceExtension> instanceExtensions = InstanceExtensionFactory.createExtensionContainer()
                .required(windowContext.getVulkanExtensions())
                .required(InstanceExtension.DEBUG_REPORT)
                .build();

        Container<InstanceLayer> instanceLayers = InstanceExtensionFactory.createLayerContainer()
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

        Instance instance = InstanceFactory.create(instanceInfo, instanceExtensions, instanceLayers);

        List<PhysicalDevice> physicalDevices = PhysicalDeviceFactory.createPhysicalDevices(instance);
        PhysicalDevice physicalDevice = physicalDevices.get(0);
        Logger.info(physicalDevice.toPropertyString());

        Surface surface = SurfaceFactory.createSurface(instance, physicalDevice, window);

        QueueContainer.Builder queueBuilder = QueueFactory.createQueueFamilies(physicalDevice);

        QueueFamily graphicsFamily = queueBuilder.required(stream -> stream.filter(family -> family.getCapabilities().contains(QueueCapability.GRAPHICS)));
        QueueFamily presentFamily = queueBuilder.required(stream -> stream.filter(family -> family.hasPresentationSupport(surface)));

        QueueContainer queueContainer = queueBuilder.build();

        Container<DeviceExtension> deviceExtensions = DeviceExtensionFactory.createExtensionContainer(physicalDevice)
                .required(DeviceExtension.KHR_SWAPCHAIN)
                .build();

        Container<DeviceFeature> deviceFeatures = DeviceExtensionFactory.createFeatureContainer(physicalDevice)
                .build();

        Device device = DeviceFactory.createDevice(physicalDevice, queueContainer, deviceExtensions, deviceFeatures);

        Queue graphicsQueue = QueueFactory.createQueue(device, graphicsFamily, 0);
        Queue presentQueue = QueueFactory.createQueue(device, presentFamily, 0);

        SwapchainInfo info = new SwapchainInfo.Builder()
                .presentQueueFamily(graphicsFamily)
                .presentQueueFamily(presentFamily)
                .build();

        Swapchain swapchain = SwapchainFactory.createSwapchain(device, surface, info);
        List<SwapchainImage> images = SwapchainFactory.createImages(device, swapchain);

        while (!window.isCloseRequested()) {
            windowContext.input();
        }

        swapchain.destroy();
        device.destroy();
        surface.destroy();
        instance.destroy();
        window.destroy();
    }
}
