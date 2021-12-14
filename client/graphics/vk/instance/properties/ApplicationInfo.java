package client.graphics.vk.instance.properties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Application information for the vulkan instance
 *
 * @see Version
 */
public class ApplicationInfo {
    private final String applicationName;
    private final String engineName;
    private final Version applicationVersion;
    private final Version engineVersion;
    private final Version apiVersion;

    /**
     * New application information instance
     *
     * @param applicationName    nullable application name
     * @param engineName         nullable engine name
     * @param applicationVersion nullable application version
     * @param engineVersion      nullable engine version
     * @param apiVersion         vulkan api version. can't be null
     * @throws IllegalArgumentException Throws if api version is null
     * @see Version
     */
    public ApplicationInfo(@Nullable String applicationName,
                           @Nullable String engineName,
                           @Nullable Version applicationVersion,
                           @Nullable Version engineVersion,
                           @NotNull Version apiVersion) {
        this.apiVersion = Objects.requireNonNull(apiVersion, "api version can't be null");

        this.applicationName = applicationName;
        this.engineName = engineName;
        this.applicationVersion = applicationVersion;
        this.engineVersion = engineVersion;
    }

    /**
     * @return application name
     */
    @Nullable
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * @return engine name
     */
    @Nullable
    public String getEngineName() {
        return engineName;
    }

    /**
     * @return application version
     * @see Version
     */
    @Nullable
    public Version getApplicationVersion() {
        return applicationVersion;
    }

    /**
     * @return engine version
     * @see Version
     */
    @Nullable
    public Version getEngineVersion() {
        return engineVersion;
    }

    /**
     * @return vulkan api version. Can't be null
     */
    @NotNull
    public Version getApiVersion() {
        return apiVersion;
    }
}
