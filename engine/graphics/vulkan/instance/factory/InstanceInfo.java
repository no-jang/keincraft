package engine.graphics.vulkan.instance.factory;

import engine.graphics.vulkan.instance.properties.Version;
import org.checkerframework.checker.nullness.qual.Nullable;

public class InstanceInfo {
    @Nullable private final String applicationName;
    @Nullable private final String engineName;
    @Nullable private final Version applicationVersion;
    @Nullable private final Version engineVersion;
    private final Version vulkanVersion;

    public InstanceInfo(@Nullable String applicationName,
                        @Nullable String engineName,
                        @Nullable Version applicationVersion,
                        @Nullable Version engineVersion,
                        Version vulkanVersion) {
        this.applicationName = applicationName;
        this.engineName = engineName;
        this.applicationVersion = applicationVersion;
        this.engineVersion = engineVersion;
        this.vulkanVersion = vulkanVersion;
    }

    @Nullable
    public String getApplicationName() {
        return applicationName;
    }

    @Nullable
    public String getEngineName() {
        return engineName;
    }

    @Nullable
    public Version getApplicationVersion() {
        return applicationVersion;
    }

    @Nullable
    public Version getEngineVersion() {
        return engineVersion;
    }

    public Version getVulkanVersion() {
        return vulkanVersion;
    }

    public static class Builder {
        @Nullable private String applicationName;
        @Nullable private String engineName;
        @Nullable private Version applicationVersion;
        @Nullable private Version engineVersion;
        private final Version vulkanVersion;

        public Builder(Version vulkanVersion) {
            this.vulkanVersion = vulkanVersion;
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

        public InstanceInfo build() {
            return new InstanceInfo(applicationName, engineName, applicationVersion, engineVersion, vulkanVersion);
        }
    }
}
