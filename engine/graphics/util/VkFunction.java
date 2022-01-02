package engine.graphics.util;

import engine.graphics.properties.VkResult;
import engine.util.enums.HasValue;

@FunctionalInterface
public interface VkFunction {
    static void execute(VkFunction function) {
        function.execute();
    }

    int getResult();

    default void execute() {
        int resultCode = getResult();

        if (resultCode == VkResult.SUCCESS.getValue()) {
            return;
        }

        VkResult result = HasValue.getByValue(resultCode, VkResult.class);
        if (result != null) {
            throw new VkException(result);
        }

        throw new RuntimeException("Unknown vulkan error: " + resultCode);
    }
}
