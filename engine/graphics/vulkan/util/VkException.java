package engine.graphics.vulkan.util;

import engine.graphics.vulkan.properties.VkResult;

public class VkException extends RuntimeException {
    private final VkResult result;

    public VkException(VkResult result) {
        super(String.format("%s (%d)", result, result.getValue()));

        this.result = result;
    }

    public VkResult getResult() {
        return result;
    }
}
