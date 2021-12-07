package client.graphics.vk.device.properties;

import client.graphics.vk.queue.QueueFamily;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeviceInfo {
    private final Map<QueueFamily, List<Float>>

    private final List<DeviceExtension> requiredExtensions;
    private final List<DeviceExtension> optionalExtensions;
    private final List<DeviceFeature> requiredFeatures;
    private final List<DeviceFeature> optionalFeatures;

    public DeviceInfo() {
        requiredExtensions = new ArrayList<>();
        optionalExtensions = new ArrayList<>();
        requiredFeatures = new ArrayList<>();
        optionalFeatures = new ArrayList<>();
    }
}
