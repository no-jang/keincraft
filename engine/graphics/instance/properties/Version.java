package engine.graphics.instance.properties;

import org.lwjgl.vulkan.VK10;

import java.util.Objects;

public class Version implements Comparable<Version> {
    private final int major;
    private final int minor;
    private final int patch;

    public Version(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public Version(int vkVersion) {
        this(VK10.VK_VERSION_MAJOR(vkVersion), VK10.VK_VERSION_MINOR(vkVersion), VK10.VK_VERSION_PATCH(vkVersion));
    }

    public int toVulkanFormat() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version = (Version) o;
        return major == version.major && minor == version.minor && patch == version.patch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch);
    }
}
