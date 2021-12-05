package client.graphics.vk.instance.models;

import java.util.ArrayList;
import java.util.List;

public class InstanceInfo {
    private final List<InstanceExtension> requiredExtensions;
    private final List<InstanceExtension> optionalExtensions;
    private final List<InstanceExtension> enabledExtensions;
    private final List<InstanceLayer> requiredLayers;
    private final List<InstanceLayer> optionalLayers;
    private final List<InstanceLayer> enabledLayers;

    public InstanceInfo(List<InstanceExtension> requiredExtensions, List<InstanceExtension> optionalExtensions, List<InstanceLayer> requiredLayers, List<InstanceLayer> optionalLayers) {
        this.requiredExtensions = requiredExtensions;
        this.optionalExtensions = optionalExtensions;
        this.requiredLayers = requiredLayers;
        this.optionalLayers = optionalLayers;

        this.enabledExtensions = new ArrayList<>();
        this.enabledLayers = new ArrayList<>();
    }

    public InstanceInfo(List<InstanceExtension> requiredExtensions, List<InstanceLayer> requiredLayers) {
        this(requiredExtensions, new ArrayList<>(), requiredLayers, new ArrayList<>());
    }

    public InstanceInfo() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    public void requiredExtension(InstanceExtension extension) {
        requiredExtensions.add(extension);
    }

    public void optionalExtension(InstanceExtension extension) {
        optionalExtensions.add(extension);
    }

    public void requiredLayer(InstanceLayer layer) {
        requiredLayers.add(layer);
    }

    public void optionalLayer(InstanceLayer layer) {
        optionalLayers.add(layer);
    }

    public List<InstanceExtension> getRequiredExtensions() {
        return requiredExtensions;
    }

    public List<InstanceExtension> getOptionalExtensions() {
        return optionalExtensions;
    }

    public List<InstanceExtension> getEnabledExtensions() {
        return enabledExtensions;
    }

    public List<InstanceLayer> getRequiredLayers() {
        return requiredLayers;
    }

    public List<InstanceLayer> getOptionalLayers() {
        return optionalLayers;
    }

    public List<InstanceLayer> getEnabledLayers() {
        return enabledLayers;
    }
}
