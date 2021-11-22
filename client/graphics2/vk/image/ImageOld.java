package client.graphics2.vk.image;

/**
 * Represent one image. This is currently only a simple wrapper
 */
// TODO Textures
public class ImageOld {
    private final long handle;

    /**
     * Creates new image wrapper object with internal vulkan handle
     *
     * @param handle internal vulkan handle
     */
    public ImageOld(long handle) {
        this.handle = handle;
    }

    /**
     * @return internal vulkan image handle
     */
    public long getHandle() {
        return handle;
    }
}
