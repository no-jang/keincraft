package client.graphics.vk.models.function;

import client.graphics.vk.instance.properties.VulkanException;
import client.graphics.vk.instance.properties.VulkanResult;
import common.util.enums.HasValue;

@FunctionalInterface
public interface CheckFunction {
    static void execute(CheckFunction function) {
        function.execute();
    }

    int getResult();

    default void execute() {
        VulkanResult result = HasValue.getByValue(getResult(), VulkanResult.class);

        if (result != VulkanResult.SUCCESS) {
            throw new VulkanException(result);
        }
    }
}
