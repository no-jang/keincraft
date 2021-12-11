import client.graphics.vk.device.LogicalDevice;
import client.graphics.vk.device.PhysicalDevice;
import client.graphics.vk.device.properties.DeviceExtension;
import client.graphics.vk.device.properties.DeviceInfo;
import client.graphics.vk.instance.VulkanInstance;
import client.graphics.vk.instance.properties.ApplicationInfo;
import client.graphics.vk.instance.properties.DebugInfo;
import client.graphics.vk.instance.properties.InstanceExtension;
import client.graphics.vk.instance.properties.InstanceInfo;
import client.graphics.vk.instance.properties.InstanceLayer;
import client.graphics.vk.instance.properties.MessageSeverity;
import client.graphics.vk.instance.properties.Version;
import client.graphics.vk.queue.QueueCapability;
import client.graphics.vk.queue.QueueFamily;
import client.graphics.vk.surface.Surface;
import client.graphics.vk.surface.Window;

import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        Window window = new Window("test", 900, 900);

        ApplicationInfo applicationInfo = new ApplicationInfo(
                "test",
                "engine",
                new Version(1, 0, 0),
                new Version(1, 0, 0),
                new Version(1, 2, 0)
        );

        InstanceInfo instanceInfo = new InstanceInfo();
        instanceInfo.addRequiredExtensions(window.getRequiredExtensions());
        instanceInfo.addRequiredExtension(InstanceExtension.DEBUG_REPORT);
        instanceInfo.addRequiredLayer(InstanceLayer.KHRONOS_VALIDATION);

        DebugInfo debugInfo = new DebugInfo(Arrays.asList(
                MessageSeverity.ERROR,
                MessageSeverity.WARNING,
                MessageSeverity.PERFORMANCE_WARNING,
                MessageSeverity.INFO,
                MessageSeverity.VERBOSE
        ));

        VulkanInstance instance = new VulkanInstance(applicationInfo, instanceInfo, debugInfo);
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
        instance.destroy();
    }
}
