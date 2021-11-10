package client.util;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;

public final class BufferUtil {
    public static void printString(PointerBuffer pBuffer) {
        int oldPosition = pBuffer.position();
        pBuffer.position(0);

        for (int i = 0; i < pBuffer.capacity(); i++) {
            System.out.println(MemoryUtil.memASCII(pBuffer.get(i)));
        }

        pBuffer.position(oldPosition);
    }
}
