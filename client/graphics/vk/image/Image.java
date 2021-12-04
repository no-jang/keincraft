package client.graphics.vk.image;

/**
 * Represent an image
 */
public class Image {
    private final long handle;
    private final int format;

    /**
     * Creates new image wrapper from existing vulkan image
     *
     * @param handle internal vulkan image handle
     * @param format image format
     */
    public Image(long handle, int format) {
        this.handle = handle;
        this.format = format;
    }

    /**
     * Gets internal vulkan image handle
     *
     * @return internal vulkan image handle
     */
    public long getHandle() {
        return handle;
    }

    /**
     * Gets format of the image
     *
     * @return image format
     */
    public int getFormat() {
        return format;
    }
}
