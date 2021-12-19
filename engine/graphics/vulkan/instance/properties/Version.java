package engine.graphics.vulkan.instance.properties;

import org.lwjgl.vulkan.VK10;

import java.util.Objects;

/**
 * Class representing a major.minor.patch version (semantic versioning). Major equals api breaking changes. Minor equals
 * additions not breaking the api Patch equals bug fixes
 */
public class Version implements Comparable<Version> {
    private final int major;
    private final int minor;
    private final int patch;

    /**
     * Creates new version from major, minor and patch number
     *
     * @param major major version (breaking changes)
     * @param minor minor version (additions)
     * @param patch patch version (bug fixes)
     */
    public Version(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    /**
     * Converts a vulkan integer version to {@link Version}
     *
     * @param version vulkan integer version
     * @return converted {@link Version}
     */
    public static Version fromVulkan(int version) {
        return new Version(VK10.VK_VERSION_MAJOR(version), VK10.VK_VERSION_MINOR(version), VK10.VK_VERSION_PATCH(version));
    }

    /**
     * Major version representing a breaking api change
     *
     * @return major version
     */
    public int getMajor() {
        return major;
    }

    /**
     * Minor version representing an addition not breaking the api
     *
     * @return minor version
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Patch version representing a bug fix
     *
     * @return patch version
     */
    public int getPatch() {
        return patch;
    }

    /**
     * Convert this {@link Version} into a vulkan integer version
     *
     * @return vulkan integer version
     */
    public int toVulkan() {
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
