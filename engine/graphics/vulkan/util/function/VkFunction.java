package engine.graphics.vulkan.util.function;

import engine.graphics.vulkan.properties.VkResult;
import engine.graphics.vulkan.util.VkException;
import engine.util.enums.HasValue;

@FunctionalInterface
public interface VkFunction {
    static void execute(VkFunction function) {
        function.execute();
    }

    int getResult();

    default void execute() {
        int resultCode = getResult();
        VkResult result = HasValue.getByValue(resultCode, VkResult.class);

        if (resultCode == VkResult.SUCCESS.getValue()) {
            return;
        }

        if (result != null) {
            throw new VkException(result);
        } else {
            throw new RuntimeException("Unknown vulkan error: " + resultCode);
        }
    }
}
