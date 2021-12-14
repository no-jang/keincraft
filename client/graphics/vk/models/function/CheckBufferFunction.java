package client.graphics.vk.models.function;

import client.graphics.vk.instance.properties.VulkanException;
import client.graphics.vk.instance.properties.VulkanResult;
import common.util.enums.HasValue;
import org.lwjgl.system.Struct;
import org.lwjgl.system.StructBuffer;

import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
public interface CheckBufferFunction<T extends Struct, B extends StructBuffer<T, B>> {
    static <T extends Struct, B extends StructBuffer<T, B>> List<T> execute(B pBuffer, CheckBufferFunction<T, B> function) {
        return function.execute(pBuffer);
    }

    int getResult(B pHandle);

    default List<T> execute(B pBuffer) {
        VulkanResult result = HasValue.getByValue(getResult(pBuffer), VulkanResult.class);

        if (result != VulkanResult.SUCCESS) {
            throw new VulkanException(result);
        }

        List<T> list = new ArrayList<>(pBuffer.capacity());
        for (int i = 0; i < pBuffer.capacity(); i++) {
            list.add(pBuffer.get(i));
        }

        return list;
    }
}
