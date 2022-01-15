package engine.collection.util;

import java.util.Arrays;

public final class ArrayUtil {
    @SuppressWarnings("unchecked")
    public static <T> T[] unsafeCast(Object[] array) {
        return (T[]) array;
    }

    public static <T> T[] unsafeCastNewArray(int length) {
        return unsafeCast(new Object[length]);
    }

    public static <T> T[] ensureCapacity(T[] array, int arraySize, int capacityMoreNeeded) {
        if (array.length - arraySize < capacityMoreNeeded) {
            return Arrays.copyOf(array, array.length + capacityMoreNeeded);
        }

        return array;
    }
}
