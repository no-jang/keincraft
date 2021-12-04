package client.graphics.vk.models.function;

import client.graphics.vk.instance.models.VulkanException;
import client.graphics.vk.instance.models.VulkanResult;
import client.graphics.vk.models.HasValue;
import org.lwjgl.PointerBuffer;

@FunctionalInterface
public interface CheckPointerFunction {
    static long execute(PointerBuffer pHandle, CheckPointerFunction function) {
        return function.execute(pHandle);
    }

    int getResult(PointerBuffer pHandle);

    default long execute(PointerBuffer pHandle) {
        VulkanResult result = HasValue.getByValue(getResult(pHandle), VulkanResult.class);

        if (result != VulkanResult.SUCCESS) {
            throw new VulkanException(result);
        }

        return pHandle.get(0);
    }
}
