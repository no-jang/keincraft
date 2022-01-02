package engine.memory.util;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.Collection;

public final class Buffers {
    public static PointerBuffer toString(MemoryStack stack, Collection<String> values) {
        PointerBuffer buffer = stack.mallocPointer(values.size());

        int i = 0;
        for (String value : values) {
            buffer.put(i, stack.ASCII(value));
            i++;
        }

        return buffer;
    }

    public static IntBuffer toInt(MemoryStack stack, Collection<Integer> values) {
        IntBuffer buffer = stack.mallocInt(values.size());

        int i = 0;
        for (int value : values) {
            buffer.put(i, value);
            i++;
        }

        return buffer;
    }

    public static LongBuffer toLong(MemoryStack stack, Collection<Long> values) {
        LongBuffer buffer = stack.mallocLong(values.size());

        int i = 0;
        for (long value : values) {
            buffer.put(i, value);
            i++;
        }

        return buffer;
    }

    public static FloatBuffer toFloat(MemoryStack stack, Collection<Float> values) {
        FloatBuffer buffer = stack.mallocFloat(values.size());

        int i = 0;
        for (float value : values) {
            buffer.put(i, value);
            i++;
        }

        return buffer;
    }
}
