package engine.graphics.instance;

public class Instance {
/*    private final VkInstance handle;
    private final Requests<InstanceExtension> extensions;
    private final Requests<InstanceLayer> layers;

    public Instance(Engine engine, VkInstance handle, Requests<InstanceExtension> extensions, Requests<InstanceLayer> layers) {
        this.handle = handle;
        this.extensions = extensions;
        this.layers = layers;
    }

    public static Builder builder(Engine engine) {
        return new Builder(engine);
    }

*//*    @Override
    protected void destroying() {
        VK10.vkDestroyInstance(handle, null);
    }*//*

    public Requests<InstanceExtension> getExtensions() {
        return extensions;
    }

    public Requests<InstanceLayer> getLayers() {
        return layers;
    }

    public VkInstance getHandle() {
        return handle;
    }

    public static class Builder extends Entity.Builder<Instance> {
        private final Engine engine;

        @Nullable
        private String applicationName;
        @Nullable
        private String engineName;
        @Nullable
        private Version applicationVersion;
        @Nullable
        private Version engineVersion;
        @Nullable
        private Version vulkanVersion;

        private Requests.@Nullable Builder<InstanceExtension> extensions;
        private Requests.@Nullable Builder<InstanceLayer> layers;

        public Builder(Engine engine) {
            this.engine = engine;
        }

        public Builder applicationName(@Nullable String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        public Builder engineName(@Nullable String engineName) {
            this.engineName = engineName;
            return this;
        }

        public Builder applicationVersion(@Nullable Version applicationVersion) {
            this.applicationVersion = applicationVersion;
            return this;
        }

        public Builder engineVersion(@Nullable Version engineVersion) {
            this.engineVersion = engineVersion;
            return this;
        }

        public Builder vulkanVersion(@Nullable Version vulkanVersion) {
            this.vulkanVersion = vulkanVersion;
            return this;
        }

        public Builder extensions(Consumer<Requests.Builder<InstanceExtension>> consumer) {
            consumer.accept(getExtensions());
            return this;
        }

        public Builder layers(Consumer<Requests.Builder<InstanceLayer>> consumer) {
            consumer.accept(getLayers());
            return this;
        }

        *//*public Instance build() {
            MemoryStack stack = engine.getEntityRegistry().getEntity(MemoryContext.class).getStack();

            Version requestedVersion = vulkanVersion != null ? vulkanVersion : new Version(1, 0, 0);
            Version availableVersion = InstanceUtil.availableVkVersion(stack);
            if (availableVersion.compareTo(requestedVersion) < 0) {
                throw new IllegalArgumentException("Requested vulkan version " + vulkanVersion +
                        " is higher than available version" + applicationVersion);
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
            Requests<InstanceExtension> extensionContainer = null;
            if (extensions != null) {
                extensionContainer = extensions.build();
                extensionBuffer = EnumBuffers.toString(stack, extensionContainer.getRequested());
            } else {
                extensionContainer = new DefaultRequests<>(List.of(), List.of());
            }

            PointerBuffer layerBuffer = null;
            Requests<InstanceLayer> layerContainer = null;
            if (layers != null) {
                layerContainer = layers.build();
                layerBuffer = EnumBuffers.toString(stack, layerContainer.getRequested());
            } else {
                layerContainer = new DefaultRequests<>(List.of(), List.of());
            }

            VkInstanceCreateInfo createInfo = VkInstanceCreateInfo.malloc(stack)
                    .sType$Default()
                    .flags(0)
                    .pNext(0)
                    .pApplicationInfo(applicationInfo)
                    .ppEnabledExtensionNames(extensionBuffer)
                    .ppEnabledLayerNames(layerBuffer);

            PointerBuffer handleBuffer = stack.mallocPointer(1);
            VkFunction.execute(() -> VK10.vkCreateInstance(createInfo, null, handleBuffer));

            VkInstance instance = new VkInstance(handleBuffer.get(0), createInfo);

            return new Instance(engine, instance, extensionContainer, layerContainer);
        }

        public Requests.Builder<InstanceExtension> getExtensions() {
            if (extensions != null) {
                return extensions;
            }

            MemoryStack stack = engine.getEntityRegistry().getEntity(MemoryContext.class).getStack();
            IntBuffer availableExtensionCountBuffer = stack.mallocInt(1);
            VkFunction.execute(() -> VK10.vkEnumerateInstanceExtensionProperties((String) null, availableExtensionCountBuffer, null));
            int availableExtensionCount = availableExtensionCountBuffer.get(0);

            VkExtensionProperties.Buffer availableExtensionsBuffer = VkExtensionProperties.malloc(availableExtensionCount, stack);
            VkFunction.execute(() -> VK10.vkEnumerateInstanceExtensionProperties((String) null, availableExtensionCountBuffer, availableExtensionsBuffer));
            List<InstanceExtension> availableExtensions =
                    EnumBuffers.ofStruct(availableExtensionsBuffer, InstanceExtension.class, VkExtensionProperties::extensionNameString);

            extensions = new DefaultRequests.Builder<>(availableExtensions);
            return extensions;
        }

        public Requests.Builder<InstanceLayer> getLayers() {
            if (layers != null) {
                return layers;
            }

            MemoryStack stack = engine.getEntityRegistry().getEntity(MemoryContext.class).getStack();
            IntBuffer availableLayerCountBuffer = stack.mallocInt(1);
            VkFunction.execute(() -> VK10.vkEnumerateInstanceLayerProperties(availableLayerCountBuffer, null));
            int availableLayerCount = availableLayerCountBuffer.get(0);

            VkLayerProperties.Buffer availableLayerBuffer = VkLayerProperties.malloc(availableLayerCount, stack);
            VkFunction.execute(() -> VK10.vkEnumerateInstanceLayerProperties(availableLayerCountBuffer, availableLayerBuffer));
            List<InstanceLayer> availableLayers =
                    EnumBuffers.ofStruct(availableLayerBuffer, InstanceLayer.class, VkLayerProperties::layerNameString);

            layers = new DefaultRequests.Builder<>(availableLayers);
            return layers;
        }*//*
    }*/
}
