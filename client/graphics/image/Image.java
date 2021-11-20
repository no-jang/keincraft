package client.graphics.image;

/**
 * Represent one image. This is currently only a simple wrapper
 */
// TODO Textures
public class Image {
    private final long handle;

    /**
     * Creates new image wrapper object with internal vulkan handle
     *
     * @param handle internal vulkan handle
     */
    public Image(long handle) {
        this.handle = handle;
    }

    /**
     * @return internal vulkan image handle
     */
    public long getHandle() {
        return handle;
    }
}
