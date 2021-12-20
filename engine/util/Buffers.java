package engine.util;

import engine.helper.enums.HasValue;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.Struct;
import org.lwjgl.system.StructBuffer;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class Buffers {
    public static <E extends Enum<E> & HasValue<String>> List<E> fromStringBuffer(PointerBuffer buffer, Class<E> enumClass) {
        List<E> values = new ArrayList<>(buffer.capacity());

        for (int i = 0; i < buffer.capacity(); i++) {
            String value = MemoryUtil.memASCII(buffer.get(i));
            E enumValue = HasValue.getByValue(value, enumClass);

            if (enumValue == null) {
                continue;
            }

            values.add(enumValue);
        }

        return values;
    }

    public static <E extends Enum<E> & HasValue<Integer>> List<E> fromIntBuffer(IntBuffer buffer, Class<E> enumClass) {
        List<E> values = new ArrayList<>(buffer.capacity());

        for (int i = 0; i < buffer.capacity(); i++) {
            int value = buffer.get(i);
            E enumValue = HasValue.getByValue(buffer.get(i), enumClass);

            if (enumValue == null) {
                continue;
            }

            values.add(enumValue);
        }

        return values;
    }

    public static <E extends Enum<E> & HasValue<Long>> List<E> fromLongBuffer(LongBuffer buffer, Class<E> enumClass) {
        List<E> values = new ArrayList<>(buffer.capacity());

        for (int i = 0; i < buffer.capacity(); i++) {
            long value = buffer.get(i);
            E enumValue = HasValue.getByValue(buffer.get(i), enumClass);

            if (enumValue == null) {
                continue;
            }

            values.add(enumValue);
        }

        return values;
    }

    public static <S extends Struct, B extends StructBuffer<S, B>, C, E extends Enum<E> & HasValue<C>>
    List<E> fromStructBuffer(B buffer, Class<E> enumClass, Function<S, C> convertFunction) {
        List<E> values = new ArrayList<>(buffer.capacity());

        for (int i = 0; i < buffer.capacity(); i++) {
            C converted = convertFunction.apply(buffer.get(i));
            E value = HasValue.getByValue(converted, enumClass);

            if (value == null) {
                continue;
            }

            values.add(value);
        }

        return values;
    }

    public static <E extends HasValue<String>> PointerBuffer toStringBuffer(MemoryStack stack, List<E> values) {
        PointerBuffer buffer = stack.mallocPointer(values.size());

        for (int i = 0; i < values.size(); i++) {
            buffer.put(i, stack.ASCII(values.get(i).getValue()));
        }

        return buffer;
    }

    public static <E extends HasValue<Integer>> IntBuffer toIntBuffer(MemoryStack stack, List<E> values) {
        IntBuffer buffer = stack.mallocInt(values.size());

        for (int i = 0; i < values.size(); i++) {
            buffer.put(i, values.get(i).getValue());
        }

        return buffer;
    }

    public static <E extends HasValue<Long>> LongBuffer toLongBuffer(MemoryStack stack, List<E> values) {
        LongBuffer buffer = stack.mallocLong(values.size());

        for (int i = 0; i < values.size(); i++) {
            buffer.put(i, values.get(i).getValue());
        }

        return buffer;
    }
}
