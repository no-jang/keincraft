import client.graphics.vk.device.PhysicalDevice;
import client.graphics.vk.instance.VulkanInstance;
import client.graphics.vk.instance.models.ApplicationInfo;
import client.graphics.vk.instance.models.DebugInfo;
import client.graphics.vk.instance.models.InstanceExtension;
import client.graphics.vk.instance.models.InstanceInfo;
import client.graphics.vk.instance.models.InstanceLayer;
import client.graphics.vk.instance.models.MessageSeverity;
import client.graphics.vk.instance.models.Version;
import client.graphics.vk.surface.Surface;
import client.graphics.vk.surface.Window;
import common.tasks.Task;
import common.tasks.TaskExecutor;
import common.tasks.TaskGraph;
import common.tasks.TaskQueue;
import org.lwjgl.vulkan.EXTDebugReport;
import org.lwjgl.vulkan.KHRSwapchain;
import org.lwjgl.vulkan.VK;

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
        instanceInfo.requiredExtensions(window.getRequiredExtensions());
        instanceInfo.requiredExtension(InstanceExtension.DEBUG_REPORT);
        instanceInfo.requiredLayer(InstanceLayer.KHRONOS_VALIDATION);

        DebugInfo debugInfo = new DebugInfo(Arrays.asList(
                MessageSeverity.ERROR,
                MessageSeverity.WARNING,
                MessageSeverity.PERFORMANCE_WARNING,
                MessageSeverity.INFO,
                MessageSeverity.VERBOSE
        ));

        VulkanInstance instance = new VulkanInstance(applicationInfo, instanceInfo, debugInfo);
        List<PhysicalDevice> physicalDevices = instance.getPhysicalDevices();
        PhysicalDevice device = physicalDevices.get(0);
        device.printDevice();

        Surface surface = new Surface(instance, window, device);

        surface.destroy();
        instance.destroy();
    }
}
