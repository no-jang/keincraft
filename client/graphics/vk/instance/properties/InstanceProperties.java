package client.graphics.vk.instance.properties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Properties on how to set up the vulkan instance.
 * Application information for debug and versioning purposes.
 * Information which extensions and validation layers are requested and which ones are enabled in the final instance.
 * Required instance extensions and validation layers will throw an exception if not available.
 * Optional instance extensions and validation layers will only log a warning if not available.
 * Information regarding the debug logger for validation layer output
 */
public class InstanceProperties {
    private final Version apiVersion;
    private final List<InstanceExtension> requiredExtensions;
    private final List<InstanceExtension> optionalExtensions;
    private final List<InstanceExtension> enabledExtensions;
    private final List<InstanceLayer> requiredLayers;
    private final List<InstanceLayer> optionalLayers;
    private final List<InstanceLayer> enabledLayers;
    private final List<MessageSeverity> severities;
    private String applicationName;
    private String engineName;
    private Version applicationVersion;
    private Version engineVersion;

    /**
     * Creates new instance properties.
     * The vulkan api version is a required property
     *
     * @param apiVersion vulkan api {@link Version}. Only major and minor version are important
     */
    public InstanceProperties(@NotNull Version apiVersion) {
        this.apiVersion = apiVersion;

        this.requiredExtensions = new ArrayList<>();
        this.optionalExtensions = new ArrayList<>();
        this.enabledExtensions = new ArrayList<>();
        this.requiredLayers = new ArrayList<>();
        this.optionalLayers = new ArrayList<>();
        this.enabledLayers = new ArrayList<>();

        this.severities = new ArrayList<>();
    }

    /**
     * Sets the name of the application shown in vulkan debug applications
     *
     * @param applicationName name of this application
     * @return this
     */
    public InstanceProperties applicationName(@Nullable String applicationName) {
        this.applicationName = applicationName;
        return this;
    }

    /**
     * Sets the name of the engine shown in vulkan debug applications
     *
     * @param engineName name of this engine
     * @return this
     */
    public InstanceProperties engineName(@Nullable String engineName) {
        this.engineName = engineName;
        return this;
    }

    /**
     * Sets the {@link Version} of the application shown in vulkan debug applications
     *
     * @param applicationVersion version of this application
     * @return this
     */
    public InstanceProperties applicationVersion(@Nullable Version applicationVersion) {
        this.applicationVersion = applicationVersion;
        return this;
    }

    /**
     * Sets the {@link Version} of the engine shown in vulkan debug applications
     *
     * @param engineVersion version of this engine
     * @return this
     */
    public InstanceProperties engineVersion(@Nullable Version engineVersion) {
        this.engineVersion = engineVersion;
        return this;
    }

    /**
     * Add a new required extensions
     *
     * @param extensions collection of required extensions
     * @return this
     * @see InstanceExtension
     */
    public InstanceProperties requiredExtension(@NotNull Collection<InstanceExtension> extensions) {
        requiredExtensions.addAll(extensions);
        return this;
    }

    /**
     * Add a new required extensions
     *
     * @param extensions array of required extensions
     * @return this
     * @see InstanceExtension
     */
    public InstanceProperties requiredExtension(@NotNull InstanceExtension... extensions) {
        Collections.addAll(requiredExtensions, extensions);
        return this;
    }

    /**
     * Add required extension
     *
     * @param extension required extension
     * @return this
     * @see InstanceExtension
     */
    public InstanceProperties requiredExtension(@NotNull InstanceExtension extension) {
        requiredExtensions.add(extension);
        return this;
    }

    /**
     * Add optional extensions
     *
     * @param extensions collection of optional extensions
     * @return this
     * @see InstanceExtension
     */
    public InstanceProperties optionalExtension(@NotNull Collection<InstanceExtension> extensions) {
        optionalExtensions.addAll(extensions);
        return this;
    }

    /**
     * Add optional extensions
     *
     * @param extensions array of optional extensions
     * @return this
     * @see InstanceExtension
     */
    public InstanceProperties optionalExtension(@NotNull InstanceExtension... extensions) {
        Collections.addAll(optionalExtensions, extensions);
        return this;
    }

    /**
     * Add optional extension
     *
     * @param extension optional extension
     * @return this
     * @see InstanceExtension
     */
    public InstanceProperties optionalExtension(@NotNull InstanceExtension extension) {
        optionalExtensions.add(extension);
        return this;
    }

    /**
     * Add required layers
     *
     * @param layers collection of required layers
     * @return this
     * @see InstanceLayer
     */
    public InstanceProperties requiredLayer(@NotNull Collection<InstanceLayer> layers) {
        requiredLayers.addAll(layers);
        return this;
    }

    /**
     * Add required layers
     *
     * @param layers array of required layers
     * @return this
     * @see InstanceLayer
     */
    public InstanceProperties requiredLayer(@NotNull InstanceLayer... layers) {
        Collections.addAll(requiredLayers, layers);
        return this;
    }

    /**
     * Add required layer
     *
     * @param layer required layer
     * @return this
     * @see InstanceLayer
     */
    public InstanceProperties requiredLayer(@NotNull InstanceLayer layer) {
        requiredLayers.add(layer);
        return this;
    }

    /**
     * Add optional layers
     *
     * @param layers collection of optional layers
     * @return this
     * @see InstanceLayer
     */
    public InstanceProperties optionalLayer(@NotNull Collection<InstanceLayer> layers) {
        optionalLayers.addAll(layers);
        return this;
    }

    /**
     * Add optional layers
     *
     * @param layers array of optional layers
     * @return this
     * @see InstanceLayer
     */
    public InstanceProperties optionalLayer(@NotNull InstanceLayer... layers) {
        Collections.addAll(optionalLayers, layers);
        return this;
    }

    /**
     * Add optional layer
     *
     * @param layer optional layer
     * @return this
     * @see InstanceLayer
     */
    public InstanceProperties optionalLayer(@NotNull InstanceLayer layer) {
        optionalLayers.add(layer);
        return this;
    }

    /**
     * Adds one message severity to be printed out by logger. The logger will check if the severity of the incoming
     * message is one of the added severities and only processes it if it is.
     *
     * @param severity {@link MessageSeverity}
     * @return this
     * @see MessageSeverity
     */
    public InstanceProperties severity(@NotNull MessageSeverity severity) {
        severities.add(severity);
        return this;
    }

    /**
     * Adds multiple message severity to be printed out by logger. The logger will check if the severity of the incoming
     * message is one of the added severities and only processes it if it is.
     *
     * @param severities {@link MessageSeverity} collection
     * @return this
     * @see MessageSeverity
     */
    public InstanceProperties severity(@NotNull Collection<MessageSeverity> severities) {
        this.severities.addAll(severities);
        return this;
    }

    /**
     * Adds multiple message severity to be printed out by logger. The logger will check if the severity of the incoming
     * message is one of the added severities and only processes it if it is.
     *
     * @param severities {@link MessageSeverity} array
     * @return this
     * @see MessageSeverity
     */
    public InstanceProperties severity(@NotNull MessageSeverity... severities) {
        Collections.addAll(this.severities, severities);
        return this;
    }

    /**
     * Returns the application name shown in vulkan debug applications
     *
     * @return application name
     */
    @Nullable
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * Returns the engine name shown in vulkan debug applications
     *
     * @return engine name
     */
    @Nullable
    public String getEngineName() {
        return engineName;
    }

    /**
     * Returns the application {@link Version} shown in vulkan debug applications
     *
     * @return application {@link Version}
     * @see Version
     */
    @Nullable
    public Version getApplicationVersion() {
        return applicationVersion;
    }

    /**
     * Returns the engine {@link Version} shown in vulkan debug applications.
     *
     * @return engine {@link Version}
     * @see Version
     */
    @Nullable
    public Version getEngineVersion() {
        return engineVersion;
    }

    /**
     * Returns the vulkan api {@link Version} that is required for this application to run
     *
     * @return vulkan api {@link Version}
     * @see Version
     */
    @NotNull
    public Version getApiVersion() {
        return apiVersion;
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

    /**
     * Return the message severity levels. The logger will check if the message has one of the configured severities
     * and only prints it then.
     *
     * @return message severity levels
     * @see MessageSeverity
     */
    @NotNull
    public List<MessageSeverity> getSeverities() {
        return severities;
    }
}
