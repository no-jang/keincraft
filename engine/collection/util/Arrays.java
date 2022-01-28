package engine.collection.util;

public final class Arrays {

    @SuppressWarnings("unchecked")
    public static <T> T[] unsafeCast(Object[] array) {
        return (T[]) array;
    }

    public static <T> T[] unsafeCastNewArray(int length) {
        return unsafeCast(new Object[length]);
    }

    public static <T> T[] ensureCapacityAtIndex(T[] array, int size, int index) {
        if (index >= size) {
            return java.util.Arrays.copyOf(array, array.length + index - size + 1);
        }
        return array;
    }
}
