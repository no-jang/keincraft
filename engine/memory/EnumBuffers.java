package engine.memory;

import engine.util.enums.HasValue;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.Struct;
import org.lwjgl.system.StructBuffer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class EnumBuffers {
    public static <E extends Enum<E> & HasValue<String>> List<E> ofString(PointerBuffer buffer, Class<E> enumClass) {
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

    public static <E extends Enum<E> & HasValue<Integer>> List<E> ofInt(IntBuffer buffer, Class<E> enumClass) {
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

    public static <E extends Enum<E> & HasValue<Long>> List<E> ofLong(LongBuffer buffer, Class<E> enumClass) {
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

    public static <E extends Enum<E> & HasValue<Float>> List<E> ofFloat(FloatBuffer buffer, Class<E> enumClass) {
        List<E> values = new ArrayList<>(buffer.capacity());

        for (int i = 0; i < buffer.capacity(); i++) {
            float value = buffer.get(i);
            E enumValue = HasValue.getByValue(buffer.get(i), enumClass);

            if (enumValue == null) {
                continue;
            }

            values.add(enumValue);
        }

        return values;
    }

    public static <S extends Struct, B extends StructBuffer<S, B>, C, E extends Enum<E> & HasValue<C>>
    List<E> ofStruct(B buffer, Class<E> enumClass, Function<S, C> convertFunction) {
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

    public static <E extends HasValue<String>> PointerBuffer toString(MemoryStack stack, List<E> values) {
        PointerBuffer buffer = stack.mallocPointer(values.size());

        for (int i = 0; i < values.size(); i++) {
            buffer.put(i, stack.ASCII(values.get(i).getValue()));
        }

        return buffer;
    }

    public static <E extends HasValue<Integer>> IntBuffer toInt(MemoryStack stack, List<E> values) {
        IntBuffer buffer = stack.mallocInt(values.size());

        for (int i = 0; i < values.size(); i++) {
            buffer.put(i, values.get(i).getValue());
        }

        return buffer;
    }

    public static <E extends HasValue<Long>> LongBuffer toLong(MemoryStack stack, List<E> values) {
        LongBuffer buffer = stack.mallocLong(values.size());

        for (int i = 0; i < values.size(); i++) {
            buffer.put(i, values.get(i).getValue());
        }

        return buffer;
    }

    public static <E extends HasValue<Float>> FloatBuffer toFloat(MemoryStack stack, List<E> values) {
        FloatBuffer buffer = stack.mallocFloat(values.size());

        for (int i = 0; i < values.size(); i++) {
            buffer.put(i, values.get(i).getValue());
        }

        return buffer;
    }
}
