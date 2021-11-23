package client;

/**
 * Every constant regarding the client.
 */
public final class ClientConstants {
    /**
     * If true:
     * vulkan validation layers are activated and vulkan debug output is printed.
     * True can produce overhead.
     */
    public static final boolean IS_DEBUG = true;

    /**
     * If true more verbose log will be shown:
     * vulkan info and debug validation output will be enabled
     */
    public static final boolean IS_VERBOSE = false;

    private ClientConstants() {
        // Empty
    }
}
