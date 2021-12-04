import client.graphics.vk.instance.VulkanInstance;
import client.graphics.vk.instance.models.ApplicationInfo;
import client.graphics.vk.instance.models.DebugInfo;
import client.graphics.vk.instance.models.InstanceInfo;
import client.graphics.vk.instance.models.MessageSeverity;
import client.graphics.vk.instance.models.Version;
import common.tasks.Task;
import common.tasks.TaskExecutor;
import common.tasks.TaskGraph;
import common.tasks.TaskQueue;
import org.lwjgl.vulkan.EXTDebugReport;
import org.lwjgl.vulkan.KHRSwapchain;
import org.lwjgl.vulkan.VK;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        ApplicationInfo applicationInfo = new ApplicationInfo(
                "test",
                "engine",
                new Version(1, 0, 0),
                new Version(1, 0, 0),
                new Version(1, 0, 0)
        );

        InstanceInfo instanceInfo = new InstanceInfo();
        instanceInfo.requiredExtension(EXTDebugReport.VK_EXT_DEBUG_REPORT_EXTENSION_NAME);
        instanceInfo.requiredLayer("VK_LAYER_KHRONOS_validation");

        DebugInfo debugInfo = new DebugInfo(Arrays.asList(
                MessageSeverity.ERROR,
                MessageSeverity.WARNING,
                MessageSeverity.PERFORMANCE_WARNING,
                MessageSeverity.INFO,
                MessageSeverity.VERBOSE
        ));

        VulkanInstance instance = new VulkanInstance(applicationInfo, instanceInfo, debugInfo);
        instance.destroy();
    }
}
