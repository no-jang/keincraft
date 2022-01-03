package engine.graphics.instance;

public class InstanceBuilder {
/*    @Nullable
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
        MemoryStack stack = getEngine().getEntityRegistry().getEntity(MemoryContext.class).getStack();

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

    @Override
    protected Instance doBuild() {
        MemoryStack stack = getEngine().getEntityRegistry().getEntity(MemoryContext.class).getStack();
        PointerBuffer handleBuffer = stack.mallocPointer(1);
            VkFunction.execute(() -> VK10.vkCreateInstance(createInfo, null, handleBuffer));

            VkInstance instance = new VkInstance(handleBuffer.get(0), createInfo);

            return new Instance(instance);
    }

    VkInstanceCreateInfo getCreateInfo() {
        return createInfo;
    }

    private Container.Builder<InstanceExtension> createExtensionContainer() {
        MemoryStack stack = getEngine().getEntityRegistry().getEntity(MemoryContext.class).getStack();
        IntBuffer availableExtensionCountBuffer = stack.mallocInt(1);
            VkFunction.execute(() -> VK10.vkEnumerateInstanceExtensionProperties((String) null, availableExtensionCountBuffer, null));
            int availableExtensionCount = availableExtensionCountBuffer.get(0);

            VkExtensionProperties.Buffer availableExtensionsBuffer = VkExtensionProperties.malloc(availableExtensionCount, stack);
            VkFunction.execute(() -> VK10.vkEnumerateInstanceExtensionProperties((String) null, availableExtensionCountBuffer, availableExtensionsBuffer));
            List<InstanceExtension> availableExtensions =
                    EnumBuffers.ofStruct(availableExtensionsBuffer, InstanceExtension.class, VkExtensionProperties::extensionNameString);

            return new DefaultContainer.Builder<>(availableExtensions);
        }

    private Container.Builder<InstanceLayer> createLayerContainer() {
        MemoryStack stack = getEngine().getEntityRegistry().getEntity(MemoryContext.class).getStack();
        IntBuffer availableLayerCountBuffer = stack.mallocInt(1);
            VkFunction.execute(() -> VK10.vkEnumerateInstanceLayerProperties(availableLayerCountBuffer, null));
            int availableLayerCount = availableLayerCountBuffer.get(0);

            VkLayerProperties.Buffer availableLayerBuffer = VkLayerProperties.malloc(availableLayerCount, stack);
            VkFunction.execute(() -> VK10.vkEnumerateInstanceLayerProperties(availableLayerCountBuffer, availableLayerBuffer));
            List<InstanceLayer> availableLayers =
                    EnumBuffers.ofStruct(availableLayerBuffer, InstanceLayer.class, VkLayerProperties::layerNameString);

            return new DefaultContainer.Builder<>(availableLayers);
    }*/
}
