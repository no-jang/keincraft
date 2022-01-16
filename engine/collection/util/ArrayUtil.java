package engine.collection.util;

import java.util.Arrays;

/**
 * Util class for interacting with array
 */
public final class ArrayUtil {

    /**
     * Casts an array without type safety at compile time. If types are not the same there will be a
     * {@link ClassCastException} on runtime.
     *
     * @param array array to cast
     * @param <T>   type of array returned
     * @return cast array to type {@code T}
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] unsafeCast(Object[] array) {
        return (T[]) array;
    }

    /**
     * Creates a new array of type {@code T} with given length. This can be used if you need to instantiate an array
     * with a generic type.
     *
     * @param length length of the created array
     * @param <T>    type of the created array
     * @return array with length and type {@code T}
     */
    public static <T> T[] unsafeCastNewArray(int length) {
        return unsafeCast(new Object[length]);
    }

    /**
     * Ensures an array has a required length. The {@code arraySize} is the number of array slots from 0 where non-null
     * values are present. The {@code capacityMoreNeeded} is the number of array slots that are needed on top of the
     * {@code arraySize}
     *
     * @param array              array
     * @param arraySize          number of elements behind each other from 0 in the array
     * @param capacityMoreNeeded the number of required slots on top of {@code arraySize}
     * @param <T>                type of the array
     * @return array with right capacity
     */
    public static <T> T[] ensureCapacity(T[] array, int arraySize, int capacityMoreNeeded) {
        if (array.length - arraySize < capacityMoreNeeded) {
            return Arrays.copyOf(array, array.length + capacityMoreNeeded);
        }

        return array;
    }
}
