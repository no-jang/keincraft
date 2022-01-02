package engine.graphics.instance;

import engine.ecs.entity.EntityBuilder;
import engine.graphics.instance.properties.Version;
import engine.graphics.util.VkFunction;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;

import java.nio.ByteBuffer;

public class InstanceBuilder extends EntityBuilder<Instance> {
    @Nullable
    private String applicationName;
    @Nullable
    private String engineName;
    @Nullable
    private Version applicationVersion;
    @Nullable
    private Version engineVersion;
    private Version vulkanVersion;

    public InstanceBuilder(Version vulkanVersion) {
        this.vulkanVersion = vulkanVersion;
    }

    public InstanceBuilder applicationName(@Nullable String applicationName) {
        this.applicationName = applicationName;
        return this;
    }

    public InstanceBuilder engineName(@Nullable String engineName) {
        this.engineName = engineName;
        return this;
    }

    public InstanceBuilder applicationVersion(@Nullable Version applicationVersion) {
        this.applicationVersion = applicationVersion;
        return this;
    }

    public InstanceBuilder engineVersion(@Nullable Version engineVersion) {
        this.engineVersion = engineVersion;
        return this;
    }

    public InstanceBuilder vulkanVersion(Version vulkanVersion) {
        this.vulkanVersion = vulkanVersion;
        return this;
    }

    @Override
    public Instance build() {
        MemoryStack stack = MemoryStack.stackPush();

        checkVulkanVersion(stack);

        VkApplicationInfo applicationInfo = createApplicationInfo(stack);

        VkInstanceCreateInfo createInfo = VkInstanceCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .pApplicationInfo(applicationInfo)
                .ppEnabledLayerNames(null)
                .ppEnabledExtensionNames(null);

        PointerBuffer handleBuffer = stack.mallocPointer(1);
        VkFunction.execute(() -> VK10.vkCreateInstance(createInfo, null, handleBuffer));

        VkInstance instance = new VkInstance(handleBuffer.get(0), createInfo);

        return new Instance(instance);
    }

    private void checkVulkanVersion(MemoryStack stack) {
        Version availableVkVersion = InstanceUtil.availableVkVersion(stack);
        if (availableVkVersion.compareTo(vulkanVersion) < 0) {
            throw new IllegalArgumentException("Requested vulkan version " + vulkanVersion + " is higher than" +
                    "available version" + applicationVersion);
        }
    }

    private VkApplicationInfo createApplicationInfo(MemoryStack stack) {
        ByteBuffer applicationNameBuffer = null;
        if (applicationName != null) {
            applicationNameBuffer = stack.ASCII(applicationName);
        }

        ByteBuffer engineNameBuffer = null;
        if (engineName != null) {
            engineNameBuffer = stack.ASCII(engineName);
        }

        int applicationVkVersion = 0;
        if (applicationVersion != null) {
            applicationVkVersion = applicationVersion.toVulkanFormat();
        }

        int engineVkVersion = 0;
        if (engineVersion != null) {
            engineVkVersion = engineVersion.toVulkanFormat();
        }


        return VkApplicationInfo.malloc(stack)
                .sType$Default()
                .pNext(0)
                .pApplicationName(applicationNameBuffer)
                .pEngineName(engineNameBuffer)
                .applicationVersion(applicationVkVersion)
                .engineVersion(engineVkVersion)
                .apiVersion(vulkanVersion.toVulkanFormat());
    }
}
