package engine.graphics.vulkan.properties;

public class VkException extends RuntimeException{
    private final VkResult result;

    public VkException(VkResult result) {
        super(String.format("%s (%d)", result.toString(), result.getValue()));

        this.result = result;
    }
    
    public VkResult getResult() {
        return result;
    }
}
