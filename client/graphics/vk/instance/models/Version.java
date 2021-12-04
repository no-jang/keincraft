package client.graphics.vk.instance.models;

import org.lwjgl.vulkan.VK10;

public class Version implements Comparable<Version> {
    private final int major;
    private final int minor;
    private final int patch;

    public Version(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public static Version fromVulkanVersion(int version) {
        return new Version(VK10.VK_VERSION_MAJOR(version), VK10.VK_VERSION_MINOR(version), VK10.VK_VERSION_PATCH(version));
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public int toVulkanVersion() {
        return VK10.VK_MAKE_VERSION(major, minor, patch);
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patch;
    }

    @Override
    public int compareTo(Version o) {
        int result;

        if ((result = Integer.compare(major, o.major)) != 0) {
            return result;
        } else if ((result = Integer.compare(minor, o.minor)) != 0) {
            return result;
        } else if ((result = Integer.compare(patch, o.patch)) != 0) {
            return result;
        }

        return 0;
    }
}
