package client.graphics.vk.models.function;

import client.graphics.vk.instance.properties.VulkanException;
import client.graphics.vk.instance.properties.VulkanResult;
import client.graphics.vk.models.HasValue;

import java.nio.IntBuffer;

@FunctionalInterface
public interface EnumerateFunction<T> {
    static <T> T execute(IntBuffer pCount, EnumerateFunction<T> function, EnumerateBufferFunction<T> bufferFunction) {
        return function.execute(pCount, bufferFunction);
    }

    int getResult(IntBuffer pCount, T pBuffer);

    default T execute(IntBuffer pCount, EnumerateBufferFunction<T> bufferFunction) {
        VulkanResult result = HasValue.getByValue(getResult(pCount, null), VulkanResult.class);

        if (result != VulkanResult.SUCCESS) {
            throw new VulkanException(result);
        }

        int count = pCount.get(0);
        T pBuffer = bufferFunction.mallocBuffer(count);

        result = HasValue.getByValue(getResult(pCount, pBuffer), VulkanResult.class);

        if (result != VulkanResult.SUCCESS) {
            throw new VulkanException(result);
        }

        return pBuffer;
    }

    @FunctionalInterface
    interface EnumerateBufferFunction<T> {
        T mallocBuffer(int count);
    }
}
