package engine.memory.util;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.List;

public final class Buffers {
    public static PointerBuffer toString(MemoryStack stack, List<String> values) {
        PointerBuffer buffer = stack.mallocPointer(values.size());

        for (int i = 0; i < values.size(); i++) {
            buffer.put(i, stack.ASCII(values.get(i)));
        }

        return buffer;
    }

    public static IntBuffer toInt(MemoryStack stack, List<Integer> values) {
        IntBuffer buffer = stack.mallocInt(values.size());

        for (int i = 0; i < values.size(); i++) {
            buffer.put(i, values.get(i));
        }

        return buffer;
    }

    public static LongBuffer toLong(MemoryStack stack, List<Long> values) {
        LongBuffer buffer = stack.mallocLong(values.size());

        for (int i = 0; i < values.size(); i++) {
            buffer.put(i, values.get(i));
        }

        return buffer;
    }

    public static FloatBuffer toFloat(MemoryStack stack, List<Float> values) {
        FloatBuffer buffer = stack.mallocFloat(values.size());

        for (int i = 0; i < values.size(); i++) {
            buffer.put(i, values.get(i));
        }

        return buffer;
    }
}
