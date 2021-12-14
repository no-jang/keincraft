package common.util;

import client.graphics.vk.memory.MemoryContext;
import common.util.enums.HasValue;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;
import java.util.List;

public final class Buffers {
    /**
     * Prints an PointerBuffer with a char*
     *
     * @param pBuffer PointerBuffer with a char*
     */
    public static void printString(PointerBuffer pBuffer) {
        int oldPosition = pBuffer.position();
        pBuffer.position(0);

        for (int i = 0; i < pBuffer.capacity(); i++) {
            System.out.println(MemoryUtil.memASCII(pBuffer.get(i)));
        }

        pBuffer.position(oldPosition);
    }

    public static <T extends HasValue<String>> PointerBuffer toStringBuffer(List<T> values) {
        MemoryStack stack = MemoryContext.getStack();
        PointerBuffer buffer = stack.mallocPointer(values.size());

        for (int i = 0; i < values.size(); i++) {
            buffer.put(i, stack.ASCII(values.get(i).getValue()));
        }

        return buffer;
    }

    public static <T extends HasValue<Integer>> IntBuffer toIntBuffer(List<T> values) {
        MemoryStack stack = MemoryContext.getStack();
        IntBuffer buffer = stack.mallocInt(values.size());

        for (int i = 0; i < values.size(); i++) {
            buffer.put(i, values.get(i).getValue());
        }

        return buffer;
    }
}
