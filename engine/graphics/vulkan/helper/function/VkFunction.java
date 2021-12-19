package engine.graphics.vulkan.helper.function;

import engine.graphics.vulkan.properties.VkException;
import engine.graphics.vulkan.properties.VkResult;
import engine.helper.enums.HasValue;

@FunctionalInterface
public interface VkFunction {
    int getResult();

    default void execute() {
        VkResult result = HasValue.getByValue(getResult(), VkResult.class);

        if(result != VkResult.SUCCESS) {
            throw new VkException(result);
        }
    }

    static void execute(VkFunction function) {
        function.execute();
    }
}
