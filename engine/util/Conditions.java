package engine.util;

import org.checkerframework.checker.nullness.qual.Nullable;

public final class Conditions {
    public static void greaterThan(int value, int greaterThan, String message) {
        if (value < greaterThan) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void lessThan(int value, int lessThan, String message) {
        if (value > lessThan) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void greaterEqualThan(int value, int greaterThan, String message) {
        if (value <= greaterThan) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void lessEqualThan(int value, int lessThan, String message) {
        if (value >= lessThan) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(@Nullable Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
