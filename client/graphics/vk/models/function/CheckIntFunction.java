package client.graphics.vk.models.function;

import client.graphics.vk.instance.models.VulkanException;
import client.graphics.vk.instance.models.VulkanResult;
import client.graphics.vk.models.HasValue;

import java.nio.IntBuffer;

@FunctionalInterface
public interface CheckIntFunction {
    static int execute(IntBuffer pHandle, CheckIntFunction function) {
        return function.execute(pHandle);
    }

    int getResult(IntBuffer pHandle);

    default int execute(IntBuffer pHandle) {
        VulkanResult result = HasValue.getByValue(getResult(pHandle), VulkanResult.class);

        if (result != VulkanResult.SUCCESS) {
            throw new VulkanException(result);
        }

        return pHandle.get(0);
    }
}