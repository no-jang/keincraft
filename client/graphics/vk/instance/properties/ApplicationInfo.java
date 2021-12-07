package client.graphics.vk.instance.properties;

public class ApplicationInfo {
    private final String applicationName;
    private final String engineName;
    private final Version applicationVersion;
    private final Version engineVersion;
    private final Version apiVersion;

    public ApplicationInfo(String applicationName, String engineName, Version applicationVersion, Version engineVersion, Version apiVersion) {
        this.applicationName = applicationName;
        this.engineName = engineName;
        this.applicationVersion = applicationVersion;
        this.engineVersion = engineVersion;
        this.apiVersion = apiVersion;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getEngineName() {
        return engineName;
    }

    public Version getApplicationVersion() {
        return applicationVersion;
    }

    public Version getEngineVersion() {
        return engineVersion;
    }

    public Version getApiVersion() {
        return apiVersion;
    }
}
