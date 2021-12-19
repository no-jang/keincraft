package engine.graphics.vulkan.instance.properties;

import engine.collections.list.DefaultImmutableList;
import engine.collections.list.ImmutableList;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InstanceInfo {
    @Nullable
    private final String applicationName;
    @Nullable
    private final String engineName;
    @Nullable
    private final Version applicationVersion;
    @Nullable
    private final Version engineVersion;

    private final Version vulkanVersion;
    private final ImmutableList<MessageSeverity> debugSeverities;

    public InstanceInfo(@Nullable String applicationName,
                        @Nullable String engineName,
                        @Nullable Version applicationVersion,
                        @Nullable Version engineVersion,
                        Version vulkanVersion,
                        List<MessageSeverity> debugSeverities) {
        this.applicationName = applicationName;
        this.engineName = engineName;
        this.applicationVersion = applicationVersion;
        this.engineVersion = engineVersion;
        this.vulkanVersion = vulkanVersion;

        this.debugSeverities = new DefaultImmutableList<>(debugSeverities);
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

    public ImmutableList<MessageSeverity> getDebugSeverities() {
        return debugSeverities;
    }

    public static class Builder {
        private final Version vulkanVersion;
        private final List<MessageSeverity> debugSeverities;
        @Nullable
        private String applicationName;
        @Nullable
        private String engineName;
        @Nullable
        private Version applicationVersion;
        @Nullable
        private Version engineVersion;

        public Builder(Version vulkanVersion) {
            this.vulkanVersion = vulkanVersion;
            this.debugSeverities = new ArrayList<>();
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

        public Builder debugSeverities(MessageSeverity... severities) {
            Collections.addAll(debugSeverities, severities);
            return this;
        }

        public InstanceInfo build() {
            return new InstanceInfo(
                    applicationName,
                    engineName,
                    applicationVersion,
                    engineVersion,
                    vulkanVersion,
                    debugSeverities
            );
        }
    }
}
