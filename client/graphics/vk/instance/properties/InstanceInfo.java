package client.graphics.vk.instance.properties;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Information regarding the vulkan instance especially requested and enabled instance extension and validation layers.
 * Required instance extensions and validation layers will throw an exception if not available.
 * Optional instance extensions and validation layers will only log a warning if not available.
 *
 * @see InstanceExtension
 * @see InstanceLayer
 */
public class InstanceInfo {
    private final List<InstanceExtension> requiredExtensions;
    private final List<InstanceExtension> optionalExtensions;
    private final List<InstanceExtension> enabledExtensions;
    private final List<InstanceLayer> requiredLayers;
    private final List<InstanceLayer> optionalLayers;
    private final List<InstanceLayer> enabledLayers;

    /**
     * New InstanceInfo
     *
     * @param requiredExtensions required extensions (throws exception if not available)
     * @param optionalExtensions optional extensions (log warning if not available)
     * @param requiredLayers     required layers (throws exception if not available)
     * @param optionalLayers     optional layers (log warning if not available)
     * @see InstanceExtension
     * @see InstanceLayer
     */
    public InstanceInfo(@NotNull List<InstanceExtension> requiredExtensions, @NotNull List<InstanceExtension> optionalExtensions,
                        @NotNull List<InstanceLayer> requiredLayers, @NotNull List<InstanceLayer> optionalLayers) {
        this.requiredExtensions = requiredExtensions;
        this.optionalExtensions = optionalExtensions;
        this.requiredLayers = requiredLayers;
        this.optionalLayers = optionalLayers;

        this.enabledExtensions = new ArrayList<>();
        this.enabledLayers = new ArrayList<>();
    }

    /**
     * New InstanceInfo
     * Optional extensions and layers are initialized empty
     *
     * @param requiredExtensions required extensions (throws exception if not available)
     * @param requiredLayers     required layers (throws exception if not available)
     * @see InstanceExtension
     * @see InstanceLayer
     */
    public InstanceInfo(@NotNull List<InstanceExtension> requiredExtensions, @NotNull List<InstanceLayer> requiredLayers) {
        this(requiredExtensions, new ArrayList<>(), requiredLayers, new ArrayList<>());
    }

    /**
     * New InstanceInfo
     * Required and optional extensions and layers are initialized empty
     */
    public InstanceInfo() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Add a new required extensions
     *
     * @param extensions collection of required extensions
     * @see InstanceExtension
     */
    public void addRequiredExtensions(@NotNull Collection<InstanceExtension> extensions) {
        requiredExtensions.addAll(extensions);
    }

    /**
     * Add required extension
     *
     * @param extension required extension
     * @see InstanceExtension
     */
    public void addRequiredExtension(@NotNull InstanceExtension extension) {
        requiredExtensions.add(extension);
    }

    /**
     * Add optional extensions
     *
     * @param extensions collection of optional extensions
     * @see InstanceExtension
     */
    public void optionalExtensions(@NotNull Collection<InstanceExtension> extensions) {
        optionalExtensions.addAll(extensions);
    }

    /**
     * Add optional extension
     *
     * @param extension optional extension
     * @see InstanceExtension
     */
    public void addOptionalExtension(@NotNull InstanceExtension extension) {
        optionalExtensions.add(extension);
    }

    /**
     * Add required layers
     *
     * @param layers collection of required layers
     * @see InstanceLayer
     */
    public void addRequiredLayers(@NotNull Collection<InstanceLayer> layers) {
        requiredLayers.addAll(layers);
    }

    /**
     * Add required layer
     *
     * @param layer required layer
     * @see InstanceLayer
     */
    public void addRequiredLayer(@NotNull InstanceLayer layer) {
        requiredLayers.add(layer);
    }

    /**
     * Add optional layers
     *
     * @param layers collection of optional layers
     * @see InstanceLayer
     */
    public void addOptionalLayers(@NotNull Collection<InstanceLayer> layers) {
        optionalLayers.addAll(layers);
    }

    /**
     * Add optional layer
     *
     * @param layer optional layer
     * @see InstanceLayer
     */
    public void addOptionalLayer(@NotNull InstanceLayer layer) {
        optionalLayers.add(layer);
    }

    /**
     * Returns instance extensions the vulkan instance requires running properly. If an extensions aren't available
     * an exception will be thrown
     *
     * @return required instance extensions
     * @see InstanceExtension
     */
    @NotNull
    public List<InstanceExtension> getRequiredExtensions() {
        return requiredExtensions;
    }

    /**
     * Returns optional instance extensions the vulkan instance doesn't necessary need. If an extensions aren't
     * available a warning will be logged
     *
     * @return optional instance extensions#
     * @see InstanceExtension
     */
    @NotNull
    public List<InstanceExtension> getOptionalExtensions() {
        return optionalExtensions;
    }

    /**
     * Returns instance extensions the vulkan instance loaded. These extensions can be used by the programme. This list
     * should only be modified by the VulkanInstance.
     *
     * @return enabled instance extensions
     * @see InstanceExtension
     */
    @NotNull
    public List<InstanceExtension> getEnabledExtensions() {
        return enabledExtensions;
    }

    /**
     * Returns validation layers the vulkan instance requires having. If a validation layer is not present an exception
     * will be thrown.
     *
     * @return required validation layers
     * @see InstanceLayer
     */
    @NotNull
    public List<InstanceLayer> getRequiredLayers() {
        return requiredLayers;
    }

    /**
     * Returns optional validation layers the vulkan instance doesn't necessary need. If a validation layer is not
     * present a warning will be logged.
     *
     * @return optional validation layer
     * @see InstanceLayer
     */
    @NotNull
    public List<InstanceLayer> getOptionalLayers() {
        return optionalLayers;
    }

    /**
     * Returns validation layers the vulkan instance loaded. This list should only be modified by the VulkanInstance.
     *
     * @return enabled validation layers
     * @see InstanceLayer
     */
    @NotNull
    public List<InstanceLayer> getEnabledLayers() {
        return enabledLayers;
    }
}
