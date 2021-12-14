package common.util;

public final class Objects {
    public static <T> T requireNotNull(T o, String message) {
        if (o == null) {
            throw new IllegalArgumentException(message);
        }
        return o;
    }
}
