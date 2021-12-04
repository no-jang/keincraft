package client.graphics.vk.models.function;

import client.graphics.vk.instance.models.VulkanException;
import client.graphics.vk.instance.models.VulkanResult;
import client.graphics.vk.models.HasValue;

import java.nio.LongBuffer;

@FunctionalInterface
public interface CheckLongFunction {
    static long execute(LongBuffer pHandle, CheckLongFunction function) {
        return function.execute(pHandle);
    }

    int getResult(LongBuffer pHandle);

    default long execute(LongBuffer pHandle) {
        VulkanResult result = HasValue.getByValue(getResult(pHandle), VulkanResult.class);

        if (result != VulkanResult.SUCCESS) {
            throw new VulkanException(result);
        }

        return pHandle.get(0);
    }
}
