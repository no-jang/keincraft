package engine.graphics.instance;

import engine.collections.container.Container;
import engine.collections.container.DefaultContainer;
import engine.ecs.entity.EntityBuilder;
import engine.graphics.instance.properties.InstanceExtension;
import engine.graphics.instance.properties.InstanceLayer;
import engine.graphics.instance.properties.Version;
import engine.graphics.util.VkFunction;
import engine.memory.util.EnumBuffers;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkExtensionProperties;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;
import org.lwjgl.vulkan.VkLayerProperties;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.function.Consumer;

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

    @Nullable
    private Container<InstanceExtension> extensionContainer;
    @Nullable
    private Container<InstanceLayer> layerContainer;

    @Nullable
    private VkInstanceCreateInfo createInfo;

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

    public InstanceBuilder extensions(Consumer<Container.Builder<InstanceExtension>> function) {
        Container.Builder<InstanceExtension> containerBuilder = createExtensionContainer();
        function.accept(containerBuilder);
        extensionContainer = containerBuilder.build();
        return this;
    }

    public InstanceBuilder layers(Consumer<Container.Builder<InstanceLayer>> function) {
        Container.Builder<InstanceLayer> containerBuilder = createLayerContainer();
        function.accept(containerBuilder);
        layerContainer = containerBuilder.build();
        return this;
    }

    @Override
    protected void preBuild() {
        super.preBuild();

        try (MemoryStack stack = MemoryStack.stackPush()) {
            Version availableVkVersion = InstanceUtil.availableVkVersion(stack);
            if (availableVkVersion.compareTo(vulkanVersion) < 0) {
                throw new IllegalArgumentException("Requested vulkan version " + vulkanVersion + " is higher than" +
                        "available version" + applicationVersion);
            }

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

            VkApplicationInfo applicationInfo = VkApplicationInfo.malloc(stack)
                    .sType$Default()
                    .pNext(0)
                    .pApplicationName(applicationNameBuffer)
                    .pEngineName(engineNameBuffer)
                    .applicationVersion(applicationVkVersion)
                    .engineVersion(engineVkVersion)
                    .apiVersion(vulkanVersion.toVulkanFormat());

            PointerBuffer extensionBuffer = null;
            if (extensionContainer != null) {
                extensionBuffer = EnumBuffers.toString(stack, extensionContainer.getRequested());
            }

            PointerBuffer layerBuffer = null;
            if (layerContainer != null) {
                layerBuffer = EnumBuffers.toString(stack, layerContainer.getRequested());
            }

            createInfo = VkInstanceCreateInfo.malloc(stack)
                    .sType$Default()
                    .flags(0)
                    .pNext(0)
                    .pApplicationInfo(applicationInfo)
                    .ppEnabledExtensionNames(extensionBuffer)
                    .ppEnabledLayerNames(layerBuffer);
        }
    }

    @Override
    protected Instance doBuild() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            PointerBuffer handleBuffer = stack.mallocPointer(1);
            VkFunction.execute(() -> VK10.vkCreateInstance(createInfo, null, handleBuffer));

            VkInstance instance = new VkInstance(handleBuffer.get(0), createInfo);

            return new Instance(instance);
        }
    }

    VkInstanceCreateInfo getCreateInfo() {
        return createInfo;
    }

    private Container.Builder<InstanceExtension> createExtensionContainer() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer availableExtensionCountBuffer = stack.mallocInt(1);
            VkFunction.execute(() -> VK10.vkEnumerateInstanceExtensionProperties((String) null, availableExtensionCountBuffer, null));
            int availableExtensionCount = availableExtensionCountBuffer.get(0);

            VkExtensionProperties.Buffer availableExtensionsBuffer = VkExtensionProperties.malloc(availableExtensionCount, stack);
            VkFunction.execute(() -> VK10.vkEnumerateInstanceExtensionProperties((String) null, availableExtensionCountBuffer, availableExtensionsBuffer));
            List<InstanceExtension> availableExtensions =
                    EnumBuffers.ofStruct(availableExtensionsBuffer, InstanceExtension.class, VkExtensionProperties::extensionNameString);

            return new DefaultContainer.Builder<>(availableExtensions);
        }
    }

    private Container.Builder<InstanceLayer> createLayerContainer() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer availableLayerCountBuffer = stack.mallocInt(1);
            VkFunction.execute(() -> VK10.vkEnumerateInstanceLayerProperties(availableLayerCountBuffer, null));
            int availableLayerCount = availableLayerCountBuffer.get(0);

            VkLayerProperties.Buffer availableLayerBuffer = VkLayerProperties.malloc(availableLayerCount, stack);
            VkFunction.execute(() -> VK10.vkEnumerateInstanceLayerProperties(availableLayerCountBuffer, availableLayerBuffer));
            List<InstanceLayer> availableLayers =
                    EnumBuffers.ofStruct(availableLayerBuffer, InstanceLayer.class, VkLayerProperties::layerNameString);

            return new DefaultContainer.Builder<>(availableLayers);
        }
    }
}
