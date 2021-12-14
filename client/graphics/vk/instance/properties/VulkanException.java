package client.graphics.vk.instance.properties;

/**
 * Exception representing a vulkan error with {@link VulkanResult} not equal to {@link VulkanResult#SUCCESS}
 */
public class VulkanException extends RuntimeException {
    private final VulkanResult result;

    /**
     * New vulkan exception from {@link VulkanResult}
     *
     * @param result {@link VulkanResult} not equal to {@link VulkanResult#SUCCESS}
     */
    public VulkanException(VulkanResult result) {
        super(String.format("%s (%d)", result.toString(), result.getValue()));

        this.result = result;
    }

    /**
     * VulkanResult from executed vulkan method that raised the error
     *
     * @return {@link VulkanResult} from executed vulkan method
     */
    public VulkanResult getResult() {
        return result;
    }
}
