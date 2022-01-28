package engine.collection.util;

public final class Arrays {

    @SuppressWarnings("unchecked")
    public static <T> T[] unsafeCast(Object[] array) {
        return (T[]) array;
    }

    public static <T> T[] unsafeCastNewArray(int length) {
        return unsafeCast(new Object[length]);
    }

    public static <T> T[] ensureCapacity(T[] array, int sizeInUse, int sizeMoreNeeded) {
        if (array.length - sizeInUse < sizeMoreNeeded) {
            return java.util.Arrays.copyOf(array, array.length + sizeMoreNeeded);
        }

        return array;
    }
}
