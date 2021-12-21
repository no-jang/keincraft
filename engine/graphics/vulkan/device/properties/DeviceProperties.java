package engine.graphics.vulkan.device.properties;

import engine.graphics.vulkan.instance.properties.Version;
import engine.util.enums.HasValue;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.lwjgl.vulkan.VkPhysicalDeviceProperties;

import java.nio.ByteBuffer;
import java.util.UUID;

public class DeviceProperties {
    private final int id;
    private final String name;
    @Nullable
    private final DeviceType type;
    @Nullable
    private final DeviceVendor vendor;
    private final Version vkVersion;
    private final Version driverVersion;
    private final UUID pipelineCacheUUID;

    public DeviceProperties(VkPhysicalDeviceProperties properties) {
        id = properties.deviceID();
        name = properties.deviceNameString();
        type = HasValue.getByValue(properties.deviceType(), DeviceType.class);
        vendor = HasValue.getByValue(properties.vendorID(), DeviceVendor.class);
        vkVersion = Version.ofVk(properties.apiVersion());
        driverVersion = Version.ofVk(properties.driverVersion());

        ByteBuffer pipelineCacheBuffer = properties.pipelineCacheUUID();
        pipelineCacheUUID = new UUID(pipelineCacheBuffer.getLong(), pipelineCacheBuffer.getLong());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public DeviceType getType() {
        return type;
    }

    @Nullable
    public DeviceVendor getVendor() {
        return vendor;
    }

    public Version getVkVersion() {
        return vkVersion;
    }

    public Version getDriverVersion() {
        return driverVersion;
    }

    public UUID getPipelineCacheUUID() {
        return pipelineCacheUUID;
    }

    @Override
    public String toString() {
        return "DeviceProperties[" +
                "id=" + id +
                ", name='" + name +
                ", type=" + type +
                ", vendor=" + vendor +
                ", vkVersion=" + vkVersion +
                ", driverVersion=" + driverVersion +
                ", pipelineCacheUUID=" + pipelineCacheUUID +
                ']';
    }
}
