package client.graphics.vk.instance.properties;

import java.util.ArrayList;
import java.util.Collection;
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

    public void addRequiredExtensions(Collection<InstanceExtension> extensions) {
        requiredExtensions.addAll(extensions);
    }

    public void addRequiredExtension(InstanceExtension extension) {
        requiredExtensions.add(extension);
    }

    public void optionalExtensions(Collection<InstanceExtension> extensions) {
        optionalExtensions.addAll(extensions);
    }

    public void addOptionalExtension(InstanceExtension extension) {
        optionalExtensions.add(extension);
    }

    public void addRequiredLayers(Collection<InstanceLayer> layers) {
        requiredLayers.addAll(layers);
    }

    public void addRequiredLayer(InstanceLayer layer) {
        requiredLayers.add(layer);
    }

    public void addOptionalLayers(Collection<InstanceLayer> layers) {
        optionalLayers.addAll(layers);
    }

    public void addOptionalLayer(InstanceLayer layer) {
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
