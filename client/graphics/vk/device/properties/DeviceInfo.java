package client.graphics.vk.device.properties;

import client.graphics.vk.queue.QueueFamily;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceInfo {
    private final Map<QueueFamily, List<Float>> queuePriorities;

    private final List<DeviceExtension> requiredExtensions;
    private final List<DeviceExtension> optionalExtensions;
    private final List<DeviceExtension> enabledExtensions;
    private final List<DeviceFeature> requiredFeatures;
    private final List<DeviceFeature> optionalFeatures;
    private final List<DeviceFeature> enabledFeatures;

    public DeviceInfo() {
        queuePriorities = new HashMap<>();

        requiredExtensions = new ArrayList<>();
        optionalExtensions = new ArrayList<>();
        enabledExtensions = new ArrayList<>();
        requiredFeatures = new ArrayList<>();
        optionalFeatures = new ArrayList<>();
        enabledFeatures = new ArrayList<>();
    }

    public void addQueue(QueueFamily family, List<Float> priorities) {
        queuePriorities.put(family, priorities);
    }

    public void addQueue(QueueFamily family, float priority) {
        queuePriorities.put(family, List.of(priority));
    }

    public void addQueues(Map<QueueFamily, List<Float>> queues) {
        queuePriorities.putAll(queues);
    }

    public void addRequiredExtension(DeviceExtension extension) {
        requiredExtensions.add(extension);
    }

    public void addRequiredExtensions(Collection<DeviceExtension> extensions) {
        requiredExtensions.addAll(extensions);
    }

    public void addOptionalExtension(DeviceExtension extension) {
        optionalExtensions.add(extension);
    }

    public void addOptionalExtensions(Collection<DeviceExtension> extensions) {
        optionalExtensions.addAll(extensions);
    }

    public void addRequiredFeature(DeviceFeature feature) {
        requiredFeatures.add(feature);
    }

    public void addRequiredFeatures(Collection<DeviceFeature> features) {
        requiredFeatures.addAll(features);
    }

    public void addOptionalFeature(DeviceFeature feature) {
        optionalFeatures.add(feature);
    }

    public void addOptionalFeatures(Collection<DeviceFeature> features) {
        optionalFeatures.addAll(features);
    }

    public Map<QueueFamily, List<Float>> getQueuePriorities() {
        return queuePriorities;
    }

    public List<DeviceExtension> getRequiredExtensions() {
        return requiredExtensions;
    }

    public List<DeviceExtension> getOptionalExtensions() {
        return optionalExtensions;
    }

    public List<DeviceExtension> getEnabledExtensions() {
        return enabledExtensions;
    }

    public List<DeviceFeature> getRequiredFeatures() {
        return requiredFeatures;
    }

    public List<DeviceFeature> getOptionalFeatures() {
        return optionalFeatures;
    }

    public List<DeviceFeature> getEnabledFeatures() {
        return enabledFeatures;
    }

    @Override
    public String toString() {
        return "DeviceInfo[" +
                "\n queuePriorities=" + queuePriorities +
                ",\n requiredExtensions=" + requiredExtensions +
                ",\n optionalExtensions=" + optionalExtensions +
                ",\n enabledExtensions=" + enabledExtensions +
                ",\n requiredFeatures=" + requiredFeatures +
                ",\n optionalFeatures=" + optionalFeatures +
                ",\n enabledFeatures=" + enabledFeatures +
                ']';
    }
}
