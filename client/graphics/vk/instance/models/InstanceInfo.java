package client.graphics.vk.instance.models;

import java.util.ArrayList;
import java.util.List;

public class InstanceInfo {
    private final List<String> requiredExtensions;
    private final List<String> optionalExtensions;
    private final List<String> enabledExtensions;
    private final List<String> requiredLayers;
    private final List<String> optionalLayers;
    private final List<String> enabledLayers;

    public InstanceInfo(List<String> requiredExtensions, List<String> optionalExtensions, List<String> requiredLayers, List<String> optionalLayers) {
        this.requiredExtensions = requiredExtensions;
        this.optionalExtensions = optionalExtensions;
        this.requiredLayers = requiredLayers;
        this.optionalLayers = optionalLayers;

        this.enabledExtensions = new ArrayList<>();
        this.enabledLayers = new ArrayList<>();
    }

    public InstanceInfo(List<String> requiredExtensions, List<String> requiredLayers) {
        this(requiredExtensions, new ArrayList<>(), requiredLayers, new ArrayList<>());
    }

    public InstanceInfo() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    public void requiredExtension(String extension) {
        requiredExtensions.add(extension);
    }

    public void optionalExtension(String extension) {
        optionalExtensions.add(extension);
    }

    public void requiredLayer(String layer) {
        requiredLayers.add(layer);
    }

    public void optionalLayer(String layer) {
        optionalLayers.add(layer);
    }

    public List<String> getRequiredExtensions() {
        return requiredExtensions;
    }

    public List<String> getOptionalExtensions() {
        return optionalExtensions;
    }

    public List<String> getEnabledExtensions() {
        return enabledExtensions;
    }

    public List<String> getRequiredLayers() {
        return requiredLayers;
    }

    public List<String> getOptionalLayers() {
        return optionalLayers;
    }

    public List<String> getEnabledLayers() {
        return enabledLayers;
    }
}
