package client.graphics.vk.device.properties;

import client.graphics.vk.instance.properties.Version;
import common.util.enums.HasValue;
import org.lwjgl.vulkan.VkPhysicalDeviceProperties;

import java.util.UUID;

public class DeviceProperties {
    private final Version apiVersion;
    private final Version driverVersion;
    private final int id;
    private final DeviceType type;
    private final DeviceVendor vendor;
    private final String name;
    private final UUID pipelineCacheUUUID;

    public DeviceProperties(VkPhysicalDeviceProperties properties) {
        apiVersion = Version.fromVulkan(properties.apiVersion());
        driverVersion = Version.fromVulkan(properties.driverVersion());
        id = properties.deviceID();
        type = HasValue.getByValue(properties.deviceType(), DeviceType.class);
        vendor = HasValue.getByValue(properties.vendorID(), DeviceVendor.class);
        name = properties.deviceNameString();
        pipelineCacheUUUID = new UUID(properties.pipelineCacheUUID().getLong(), properties.pipelineCacheUUID().getLong());
    }

    public Version getApiVersion() {
        return apiVersion;
    }

    public Version getDriverVersion() {
        return driverVersion;
    }

    public int getId() {
        return id;
    }

    public DeviceType getType() {
        return type;
    }

    public DeviceVendor getVendor() {
        return vendor;
    }

    public String getName() {
        return name;
    }

    public UUID getPipelineCacheUUUID() {
        return pipelineCacheUUUID;
    }

    @Override
    public String toString() {
        return "DeviceProperties[" +
                "apiVersion=" + apiVersion +
                ", driverVersion=" + driverVersion +
                ", id=" + id +
                ", type=" + type +
                ", vendor=" + vendor +
                ", name='" + name + '\'' +
                ", pipelineCacheUUUID=" + pipelineCacheUUUID +
                ']';
    }
}